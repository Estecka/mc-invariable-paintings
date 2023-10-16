package tk.estecka.invarpaint.crafting;

import java.util.HashSet;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
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
implements IUnsyncRecipe, IObfuscatedRecipe
{
	static public final Identifier ID = new Identifier("invarpaint", "crafting_special_painting_creation");
	static public final RecipeSerializer<FilledPaintingRecipe> SERIALIZER = new FilledPaintingRecipeSerializer();

	public final boolean canCreate;
	public final boolean canDerive;
	public final boolean isObfuscated;

	static public void Register(){
		Registry.register(Registries.RECIPE_SERIALIZER, FilledPaintingRecipe.ID, FilledPaintingRecipe.SERIALIZER);
	}

	public FilledPaintingRecipe(Identifier id, CraftingRecipeCategory category, boolean canCreate, boolean canDerive, boolean isObfuscated){
		super(id, category);
		this.canCreate = canCreate;
		this.canDerive = canDerive;
		this.isObfuscated = isObfuscated;
	}

	@Override
	public boolean IsObfuscated(){
		return this.isObfuscated;
	}

	private boolean ValidatePainting(ItemStack painting, World world){
		boolean hasVariant = PaintStackUtil.HasVariantId(painting);

		return (canCreate && !hasVariant) || (canDerive && hasVariant);
	}

	public boolean matches(CraftingInventory ingredients, World world){
		boolean hasPainting = false;
		var dyeSet = new HashSet<DyeItem>(8);

		for (int i=0; i<ingredients.size(); ++i){
			ItemStack stack = ingredients.getStack(i);
			if (stack.getItem() instanceof DyeItem){
				DyeItem dye = (DyeItem)stack.getItem();
				if(dyeSet.contains(dye))
					return false;
				dyeSet.add(dye);
			}
			else if (!hasPainting && stack.isOf(Items.PAINTING)){
				if(!ValidatePainting(stack, world))
					return false;
				hasPainting = true;
			}
			else if (!stack.isEmpty())
				return false;
		}

		return hasPainting && (dyeSet.size() == 8);
	}

	public ItemStack craft(CraftingInventory ingredients, DynamicRegistryManager manager){
		short dyeMask = 0;
		for (int i=0; i<ingredients.size(); ++i){
			if (ingredients.getStack(i).getItem() instanceof DyeItem)
				dyeMask |= 1 << ((DyeItem)ingredients.getStack(i).getItem()).getColor().getId();
		}

		long dyeCode = DyeCodeUtil.MaskToCode(dyeMask);

		var entry = DyeCodeUtil.DyemaskToVariant(dyeMask);
		if (entry.isPresent()){
			return PaintStackUtil.CreateVariant(entry.get().getKey().get().getValue().toString());
		}
		else {
			InvariablePaintings.LOGGER.error("Unable to find a valid painting: {}", String.format("0x%08X", dyeCode));
			return ItemStack.EMPTY;
		}
	}

	public boolean fits(int width, int height){
		return (width*height) >= 9;
	}

	@Override
	public RecipeSerializer<FilledPaintingRecipe> getSerializer(){
		return SERIALIZER;
	}
}
