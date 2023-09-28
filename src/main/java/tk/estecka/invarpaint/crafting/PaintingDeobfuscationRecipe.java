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
import net.minecraft.world.World;
import tk.estecka.invarpaint.PaintStackUtil;

public class PaintingDeobfuscationRecipe 
extends SpecialCraftingRecipe
{
	static public final Identifier ID = new Identifier("invarpaint", "crafting_special_painting_deobfuscation");
	static public final SpecialRecipeSerializer<PaintingDeobfuscationRecipe> SERIALIZER = new SpecialRecipeSerializer<PaintingDeobfuscationRecipe>(PaintingDeobfuscationRecipe::new);

	static public void Register(){
		Registry.register(Registries.RECIPE_SERIALIZER, PaintingDeobfuscationRecipe.ID, PaintingDeobfuscationRecipe.SERIALIZER);
	}

	public PaintingDeobfuscationRecipe(Identifier id, CraftingRecipeCategory category){
		super(id, category);
	}

	public boolean matches(CraftingInventory ingredients, World world){
		boolean hasPainting = false;
		int dyeCount = 0;

		for (int i=0; i<ingredients.size(); ++i){
			ItemStack stack = ingredients.getStack(i);
			if (stack.isOf(Items.GLOW_INK_SAC))
				dyeCount++;
			else if (!hasPainting && stack.isOf(Items.PAINTING) && PaintStackUtil.HasDyeCode(stack))
				hasPainting = true;
			else
				return false;
		}

		return hasPainting && (dyeCount==8);
	}

	public ItemStack craft(CraftingInventory ingredients, DynamicRegistryManager manager){
		for (int i=0; i<ingredients.size(); ++i){
			if (ingredients.getStack(i).isOf(Items.PAINTING)){
				var variant = DyeCodeUtil.DyemaskToVariant(DyeCodeUtil.CodeToMask(PaintStackUtil.GetDyeCode(ingredients.getStack(i)), 8));
				if (variant.isPresent())
					return PaintStackUtil.CreateVariant(variant.get().getKey().get().getValue().toString());
			}
		}

		return ItemStack.EMPTY;
	}

	public boolean fits(int width, int height){
		return (width*height) >= 9;
	}

	public SpecialRecipeSerializer<PaintingDeobfuscationRecipe> getSerializer(){
		return SERIALIZER;
	}
}
