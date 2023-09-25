package tk.estecka.invarpaint;

import net.minecraft.entity.decoration.painting.PaintingVariant;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FilledPaintingRecipe 
extends SpecialCraftingRecipe
{
	static public final Identifier ID = new Identifier("invarpaint", "crafting_special_painting");
	static public final SpecialRecipeSerializer<FilledPaintingRecipe> SERIALIZER = new SpecialRecipeSerializer<FilledPaintingRecipe>(FilledPaintingRecipe::new);

	static public void Register(){
		Registry.register(Registries.RECIPE_SERIALIZER, FilledPaintingRecipe.ID, FilledPaintingRecipe.SERIALIZER);
	}

	public FilledPaintingRecipe(Identifier id, CraftingRecipeCategory category){
		super(id, category);
	}

	public boolean matches(CraftingInventory ingredients, World manager){
		boolean hasPainting = false;
		boolean hasDyes = false;

		for (int i=0; i<ingredients.size(); ++i){
			ItemStack item = ingredients.getStack(i);
			if (item.getItem() instanceof DyeItem)
				hasDyes = true;
			else if (!hasPainting && item.isOf(Items.PAINTING))
				hasPainting = true;
			else if (!item.isEmpty())
				return false;
		}

		return hasPainting && hasDyes;
	}

	public ItemStack craft(CraftingInventory ingredients, DynamicRegistryManager manager){
		int dyeCode = 0;
		int offset = 0;
		for (int i=0; i<ingredients.size(); ++i){
			ItemStack item = ingredients.getStack(i);
			if (!(item.getItem() instanceof DyeItem))
				++offset;
			else {
				DyeItem dye = (DyeItem)item.getItem();
				dyeCode |= dye.getColor().getId() << 4*(i-offset);
			}
		}

		RegistryEntry<PaintingVariant> entry;
		try{
			entry = Registries.PAINTING_VARIANT.getIndexedEntries().getOrThrow(dyeCode);
		}
		catch (IllegalArgumentException e){
			return ItemStack.EMPTY;
		}
		return PaintStackUtil.CreateVariant(entry.getKey().get().getValue().toString());
	}

	public boolean fits(int width, int height){
		return (2 <= width) && (2 <= height);
	}

	public SpecialRecipeSerializer<FilledPaintingRecipe> getSerializer(){
		return SERIALIZER;
	}
}
