package tk.estecka.invarpaint;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import static net.minecraft.component.DataComponentTypes.ENTITY_DATA;

public class PaintStackUtil
{
	static public final String OBFUSCATED_TAG = "obfuscated";
	static public final String VARIANT_TAG = "variant";
	static public final String ENTITY_TYPE_TAG = "id";

	static public ItemStack	SetVariant(ItemStack stack, @NotNull String variantId){
		NbtCompound entityTag;

		NbtComponent component = stack.get(ENTITY_DATA);
		if (component == null) 
			entityTag = new NbtCompound();
		else {
			InvariablePaintings.LOGGER.warn("Existing `EntityData` is is being overwritten.");
			entityTag = component.copyNbt();
		}

		if (entityTag.contains(ENTITY_TYPE_TAG))
			InvariablePaintings.LOGGER.warn("Existing `EntityData.id` is being overwritten.");
		entityTag.putString(ENTITY_TYPE_TAG, "minecraft:painting");

		if (entityTag.contains(VARIANT_TAG))
			InvariablePaintings.LOGGER.warn("Existing `EntityData.variant` is being overwritten.");
		entityTag.putString(VARIANT_TAG, variantId);

		stack.set(ENTITY_DATA, NbtComponent.of(entityTag));
		return stack;
	}

	static public ItemStack	SetRandomVariant(ItemStack stack, Random random){
		var variant = Registries.PAINTING_VARIANT.getRandom(random);
		if (variant.isPresent())
			return SetVariant( stack, Registries.PAINTING_VARIANT.getId(variant.get().value()).toString() );
		else {
			InvariablePaintings.LOGGER.error("Unable to pull a random variant from the registry.");
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

}
