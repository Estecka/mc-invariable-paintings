package fr.estecka.invarpaint.api;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
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
		return SetVariant(stack, GetVariantEntry(entity));
	}

	// TODO
	@Deprecated
	static public ItemStack	SetVariant(ItemStack stack, @NotNull Identifier variantId) {
		throw new NotImplementedException();
	}

	// TODO
	@Deprecated
	static public ItemStack	SetVariant(ItemStack stack, @NotNull String variantName){
		throw new NotImplementedException();
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
		return CreateVariant(GetVariantEntry(entity));
	}

	// TODO
	@Deprecated
	static public ItemStack	CreateVariant(Identifier variantId){
		throw new NotImplementedException();
	}

	// TODO
	@Deprecated
	static public ItemStack	CreateVariant(String variantName){
		throw new NotImplementedException();
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
			stack.set(DataComponentTypes.ITEM_MODEL, INVALID_MODEL);
			return stack;
		}
	}

	static public ItemStack SetModel(ItemStack stack, Identifier variantId){
		stack.set(DataComponentTypes.ITEM_MODEL, variantId.withPrefixedPath(VARIANT_MODEL_PREFIX));
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

	// TODO: Probably incompatible with NoKebab
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
