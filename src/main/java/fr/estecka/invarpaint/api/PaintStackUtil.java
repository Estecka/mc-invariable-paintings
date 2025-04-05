package fr.estecka.invarpaint.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import static net.minecraft.component.DataComponentTypes.ITEM_MODEL;
import static net.minecraft.component.DataComponentTypes.PAINTING_VARIANT;
import static fr.estecka.invarpaint.InvarpaintMod.CONFIG;

public final class PaintStackUtil
{
	static public final Identifier INVALID_MODEL = Identifier.of("invarpaint", "missing_painting");
	static public final String VARIANT_MODEL_PREFIX = "painting/";


/******************************************************************************/
/* # Set Variant                                                              */
/******************************************************************************/

	static public ItemStack	SetVariant(ItemStack stack, @NotNull RegistryEntry<PaintingVariant> entry) {
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

	static public ItemStack	CreateVariant(@NotNull RegistryEntry<PaintingVariant> entry) {
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

	static public ItemStack	SetModel(ItemStack stack, @NotNull RegistryEntry<PaintingVariant> entry) {
		return SetModel(stack, entry.getKey().get().getValue());
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
		stack.set(ITEM_MODEL, variantId.withPrefixedPath(VARIANT_MODEL_PREFIX));
		return stack;
	}


/******************************************************************************/
/* # Get Variant                                                              */
/******************************************************************************/
// TODO: Probably not compatible with NoKebab

	static public @Nullable RegistryEntry<PaintingVariant>	GetVariantEntry(ItemStack stack){
		return stack.get(PAINTING_VARIANT);
	}

	static public @Nullable RegistryEntry<PaintingVariant>	GetVariantEntry(Entity entity){
		return entity.get(PAINTING_VARIANT);
	}

	static public @Nullable Identifier	GetVariantId(ItemStack stack){
		var entry = GetVariantEntry(stack);
		return (entry == null) ? null : entry.getKey().get().getValue();
	}

	@Deprecated
	static public @Nullable String	GetVariantName(Entity entity){
		var entry = entity.get(PAINTING_VARIANT);
		return (entry == null) ? null : entry.getKey().get().getValue().toString();
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
		return stack.contains(PAINTING_VARIANT);
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
