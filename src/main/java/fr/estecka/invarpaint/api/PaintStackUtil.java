package fr.estecka.invarpaint.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import static net.minecraft.core.component.DataComponents.ITEM_MODEL;
import static net.minecraft.core.component.DataComponents.PAINTING_VARIANT;
import static fr.estecka.invarpaint.InvarpaintMod.CONFIG;

public final class PaintStackUtil
{
	static public final Identifier INVALID_MODEL = Identifier.fromNamespaceAndPath("invarpaint", "missing_painting");
	static public final String VARIANT_MODEL_PREFIX = "painting/";


/******************************************************************************/
/* # Set Variant                                                              */
/******************************************************************************/

	static public ItemStack SetVariant(ItemStack stack, @NotNull Holder<PaintingVariant> entry) {
		stack.set(PAINTING_VARIANT, entry);
		if (CONFIG.setItemModel)
			SetModel(stack, entry);
		return stack;
	}

	static public ItemStack	SetVariant(ItemStack stack, @NotNull Entity entity) {
		var entry = GetVariantEntry(entity);
		if (entry != null)
			SetVariant(stack, entry);
		return stack;
	}


/******************************************************************************/
/* # Create Variant                                                           */
/******************************************************************************/

	static public ItemStack CreateVariant(@NotNull Holder<PaintingVariant> entry) {
		ItemStack stack = new ItemStack(Items.PAINTING);
		SetVariant(stack, entry);
		if (CONFIG.setItemModel)
			SetModel(stack, entry);
		return stack;
	}

	static public ItemStack	CreateVariant(Entity entity){
		var entry = GetVariantEntry(entity);
		if (entry != null)
			return CreateVariant(entry);
		else
			return new ItemStack(Items.PAINTING);
	}


/******************************************************************************/
/* # Set Model                                                                */
/******************************************************************************/

	static public ItemStack SetModel(ItemStack stack, @NotNull Holder<PaintingVariant> entry) {
		return SetModel(stack, entry.unwrapKey().get().identifier());
	}

	static public ItemStack SetModel(ItemStack stack, String variantName){
		Identifier id = Identifier.tryParse(variantName);
		if (id != null)
			return SetModel(stack, id);
		else {
			stack.set(ITEM_MODEL, INVALID_MODEL);
			return stack;
		}
	}

	static public ItemStack SetModel(ItemStack stack, Identifier variantId){
		stack.set(ITEM_MODEL, variantId.withPrefix(VARIANT_MODEL_PREFIX));
		return stack;
	}


/******************************************************************************/
/* # Get Variant                                                              */
/******************************************************************************/
// Probably not compatible with NoKebab

	static public @Nullable Holder<PaintingVariant> GetVariantEntry(ItemStack stack){
		return stack.get(PAINTING_VARIANT);
	}

	static public @Nullable Holder<PaintingVariant> GetVariantEntry(Entity entity){
		return entity.get(PAINTING_VARIANT);
	}

	static public @Nullable Identifier	GetVariantId(ItemStack stack){
		var entry = GetVariantEntry(stack);
		return (entry == null) ? null : entry.unwrapKey().get().identifier();
	}

	@Deprecated
	static public @Nullable String	GetVariantName(Entity entity){
		var entry = entity.get(PAINTING_VARIANT);
		return (entry == null) ? null : entry.unwrapKey().get().identifier().toString();
	}

	@Deprecated
	static public @Nullable String	GetVariantName(ItemStack stack){
		var id = GetVariantId(stack);
		return (id == null) ? null : id.toString();
	}


/******************************************************************************/
/* # HasVariant                                                               */
/******************************************************************************/

	/**
	 * Checks that a variant exists in any form
	 */
	static public boolean	HasVariant(ItemStack stack){
		return stack.has(PAINTING_VARIANT);
	}

	/**
	 * Checks that the variant is a valid identifier.
	 * @deprecated Stacks can no longer contains invalid variants.
	 */
	@Deprecated
	static public boolean	HasVariantId(ItemStack stack){
		return HasVariant(stack);
	}
}
