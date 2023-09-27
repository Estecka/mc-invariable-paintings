package tk.estecka.invarpaint.crafting;

import java.util.HashSet;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import tk.estecka.invarpaint.InvariablePaintings;
import tk.estecka.invarpaint.PaintStackUtil;

public class FilledPaintingRecipe 
extends SpecialCraftingRecipe
{
	static public final Identifier ID = new Identifier("invarpaint", "crafting_special_painting_creation");
	static public final SpecialRecipeSerializer<FilledPaintingRecipe> SERIALIZER = new SpecialRecipeSerializer<FilledPaintingRecipe>(FilledPaintingRecipe::new);

	static public void Register(){
		Registry.register(Registries.RECIPE_SERIALIZER, FilledPaintingRecipe.ID, FilledPaintingRecipe.SERIALIZER);
	}

	public FilledPaintingRecipe(Identifier id, CraftingRecipeCategory category){
		super(id, category);
	}

	public boolean matches(CraftingInventory ingredients, World world){
		boolean hasPainting = false;
		var dyes = new HashSet<DyeItem>(8);

		for (int i=0; i<ingredients.size(); ++i){
			ItemStack stack = ingredients.getStack(i);
			if (stack.getItem() instanceof DyeItem)
				dyes.add((DyeItem)stack.getItem());
			else if (!hasPainting && stack.isOf(Items.PAINTING))
				hasPainting = true;
			else if (!stack.isEmpty())
				return false;
		}

		return hasPainting && (dyes.size() == 8);
	}

	public ItemStack craft(CraftingInventory ingredients, DynamicRegistryManager manager){
		short dyeMask = 0;
		for (int i=0; i<ingredients.size(); ++i){
			if (ingredients.getStack(i).getItem() instanceof DyeItem)
				dyeMask |= 1 << ((DyeItem)ingredients.getStack(i).getItem()).getColor().getId();
		}

		long dyeCode = DyeCodeUtil.MaskToCode(dyeMask);
		int index = DyeCodeUtil.CombinationToIndex(dyeCode, 8);
		InvariablePaintings.LOGGER.info("Crafted {} from {}", index, String.format("0x%08X", dyeCode));
		index %= Registries.PAINTING_VARIANT.size();

		var entry = Registries.PAINTING_VARIANT.getEntry(index);
		if (entry.isPresent())
			return PaintStackUtil.CreateVariant(entry.get().getKey().get().getValue().toString());
		else
			return ItemStack.EMPTY;
	}

	public boolean fits(int width, int height){
		return (width*height) >= 9;
	}

	public SpecialRecipeSerializer<FilledPaintingRecipe> getSerializer(){
		return SERIALIZER;
	}
}
