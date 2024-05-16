package tk.estecka.invarpaint.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;
import static net.minecraft.component.DataComponentTypes.ENTITY_DATA;
import static tk.estecka.invarpaint.InvarpaintMod.LOGGER;

public class PaintStackUtil
{
	static private final String VARIANT_TAG = "variant";
	static private final String ENTITY_TYPE_TAG = "id";

	static public ItemStack	SetVariant(ItemStack stack, @NotNull String variantId){
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
		entityTag.putString(VARIANT_TAG, variantId);

		stack.set(ENTITY_DATA, NbtComponent.of(entityTag));
		return stack;
	}

	static public ItemStack	SetRandomVariant(ItemStack stack, Random random){
		var variant = Registries.PAINTING_VARIANT.getRandom(random);
		if (variant.isPresent())
			return SetVariant( stack, Registries.PAINTING_VARIANT.getId(variant.get().value()).toString() );
		else {
			LOGGER.error("Unable to pull a random variant from the registry.");
			return stack;
		}
	}

	static public ItemStack	CreateRandomVariant(Random random){
		return SetRandomVariant(new ItemStack(Items.PAINTING), random);
	}

	static public ItemStack	CreateVariant(String variantId){
		return SetVariant(new ItemStack(Items.PAINTING), variantId);
	}

	static public @Nullable String	GetVariantId(ItemStack stack){
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

	static public MutableText TranslatableVariantName(String variantId){
		return Text.translatableWithFallback("painting."+variantId.replace(":",".")+".title", variantId);
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
