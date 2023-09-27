package tk.estecka.invarpaint.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import tk.estecka.invarpaint.InvariablePaintings;
import tk.estecka.invarpaint.PaintStackUtil;

public class PaintingReplicationRecipe 
extends SpecialCraftingRecipe
{
	static public final Identifier ID = new Identifier("invarpaint", "crafting_special_painting_replication");
	static public final SpecialRecipeSerializer<PaintingReplicationRecipe> SERIALIZER = new SpecialRecipeSerializer<PaintingReplicationRecipe>(PaintingReplicationRecipe::new);

	static public void Register(){
		Registry.register(Registries.RECIPE_SERIALIZER, PaintingReplicationRecipe.ID, PaintingReplicationRecipe.SERIALIZER);
	}

	public PaintingReplicationRecipe(Identifier id, CraftingRecipeCategory category){
		super(id, category);
	}

	public boolean matches(CraftingInventory ingredients, World world){
		int dyeCount = 0;
		boolean hasCanvas = false;
		boolean hasTemplate = false;

		if (!world.getGameRules().getBoolean(InvariablePaintings.REPLICA_RULE))
			return false;

		for (int i=0; i<ingredients.size(); ++i){
			ItemStack stack = ingredients.getStack(i);
			if (stack.isOf(Items.GLOW_INK_SAC))
				dyeCount++;
			else if (!stack.isOf(Items.PAINTING))
				return false;
			else if (!hasCanvas && !PaintStackUtil.HasVariantId(stack))
				hasCanvas = true;
			else if (!hasTemplate && PaintStackUtil.HasVariantId(stack))
				hasTemplate = true;
			else
				return false;
		}

		return hasCanvas && hasTemplate && (dyeCount>=7);
	}

	public ItemStack craft(CraftingInventory ingredients, DynamicRegistryManager manager){
		for (int i=0; i<ingredients.size(); ++i)
			if (PaintStackUtil.HasVariantId(ingredients.getStack(i)))
				return ingredients.getStack(i).copyWithCount(1);

		return ItemStack.EMPTY;
	}

	public DefaultedList<ItemStack> getRemainder(CraftingInventory ingredients) {
		DefaultedList<ItemStack> remainder = DefaultedList.ofSize(ingredients.size(), ItemStack.EMPTY);
		for (int i=0; i<ingredients.size(); ++i)
			if (PaintStackUtil.HasVariantId(ingredients.getStack(i)))
				remainder.set(i, ingredients.getStack(i).copyWithCount(1));

		return remainder;
	}

	public boolean fits(int width, int height){
		return (width*height) >= 9;
	}

	public SpecialRecipeSerializer<PaintingReplicationRecipe> getSerializer(){
		return SERIALIZER;
	}
}
