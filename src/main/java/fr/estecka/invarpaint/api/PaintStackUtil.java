package fr.estecka.invarpaint.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;
import static net.minecraft.component.DataComponentTypes.ENTITY_DATA;
import static fr.estecka.invarpaint.InvarpaintMod.CONFIG;
import static fr.estecka.invarpaint.InvarpaintMod.LOGGER;

public class PaintStackUtil
{
	static public final Identifier INVALID_MODEL = Identifier.of("invarpaint", "item/missing_painting");
	static public final String VARIANT_MODEL_PREFIX = "painting/";

	static private final String VARIANT_TAG = "variant";
	static private final String ENTITY_TYPE_TAG = "id";


	static public ItemStack	SetVariant(ItemStack stack, @NotNull Entity entity) { return SetVariant(stack, GetVariantName(entity)); }
	static public ItemStack	SetVariant(ItemStack stack, @NotNull Identifier variantId) { return SetVariant(stack, variantId.toString()); }
	static public ItemStack	SetVariant(ItemStack stack, @NotNull String variantName){
		NbtCompound entityTag;

		NbtComponent component = stack.get(ENTITY_DATA);
		if (component == null) 
			entityTag = new NbtCompound();
		else {
			LOGGER.warn("Existing `EntityData` is is being overwritten.");
			entityTag = component.copyNbt();
		}

		if (entityTag.contains(ENTITY_TYPE_TAG))
			LOGGER.warn("Existing `EntityData.id` is being overwritten.");
		entityTag.putString(ENTITY_TYPE_TAG, "minecraft:painting");

		if (entityTag.contains(VARIANT_TAG))
			LOGGER.warn("Existing `EntityData.variant` is being overwritten.");
		entityTag.putString(VARIANT_TAG, variantName);

		stack.set(ENTITY_DATA, NbtComponent.of(entityTag));
		if (CONFIG.setItemModel)
			SetModel(stack, variantName);
		return stack;
	}

	@Deprecated
	static public ItemStack	SetRandomVariant(ItemStack stack, Random random, Registry<PaintingVariant> registry){
		var variant = registry.getRandom(random);
		if (variant.isPresent())
			return SetVariant( stack, variant.get().getKey().get().getValue() );
		else {
			LOGGER.error("Unable to pull a random variant from the registry.");
			return stack;
		}
	}

	@Deprecated
	static public ItemStack	CreateRandomVariant(Random random, Registry<PaintingVariant> registry){
		return SetRandomVariant(new ItemStack(Items.PAINTING), random, registry);
	}

	static public ItemStack	CreateVariant(Entity entity){ return CreateVariant(GetVariantName(entity)); }
	static public ItemStack	CreateVariant(Identifier variantId){ return CreateVariant(variantId.toString()); }
	static public ItemStack	CreateVariant(String variantName){
		ItemStack stack = new ItemStack(Items.PAINTING);
		SetVariant(stack, variantName);
		if (CONFIG.setItemModel)
			SetModel(stack, variantName);
		return stack;
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

	/**
	 * @deprecated Will be changed to return an Identifier in a future release.
	 * Replaced by `GetVariantName`
	 */
	@Deprecated
	static public @Nullable String	GetVariantId(ItemStack stack){
		return GetVariantName(stack);
	}

	/**
	 * @implNote This particular implementation is compatible with NoKebab.
	 */
	static public @Nullable String	GetVariantName(Entity entity){
		return entity.writeNbt(new NbtCompound()).getString("variant");
	}

	static public @Nullable String	GetVariantName(ItemStack stack){
		NbtComponent entitydata = stack.get(ENTITY_DATA);
		if (entitydata == null || !entitydata.contains(VARIANT_TAG))
			return null;

		return entitydata.copyNbt().getString(VARIANT_TAG);
	}

	static public boolean	HasVariantId(ItemStack stack){
		NbtComponent nbt = stack.get(ENTITY_DATA);
		return nbt != null
		    && nbt.contains(VARIANT_TAG)
		    ;
	}

	static public MutableText TranslatableVariantName(Identifier variantId){
		return Text.translatableWithFallback(variantId.toTranslationKey("painting", "title"), variantId.toString());
	}
	static public MutableText TranslatableVariantName(String variantName){
		return Text.translatableWithFallback("painting."+variantName.replace(":",".")+".title", variantName);
	}

	/**
	 * @deprecated Only used by the crafting addon.
	 */
	@Deprecated
	static public boolean IsObfuscated(ItemStack stack){
		NbtComponent nbt = stack.get(CUSTOM_DATA);
		return nbt != null
		    && nbt.contains("invarpaint:obfuscated")
		    ;
	}

}
