package tk.estecka.invarpaint;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;

public class PaintStackUtil
{
	static public final String ENTITY_TAG = "EntityTag";
	static public final String VARIANT_TAG = "variant";

	static public ItemStack	SetVariant(ItemStack stack, String variantId){
		if (!stack.isOf(Items.PAINTING))
			InvariablePaintings.LOGGER.warn("Setting painting variant on non-painting item. (Type: {})", stack.getItem());
		
		NbtCompound nbt = stack.getOrCreateNbt();
		NbtCompound entityTag;
		byte entityTagType = nbt.getType(ENTITY_TAG);

		if (entityTagType == NbtElement.COMPOUND_TYPE)
			entityTag = nbt.getCompound(ENTITY_TAG);
		else {
			if (entityTagType != NbtElement.END_TYPE)
				InvariablePaintings.LOGGER.warn("Existing `EntityTag` is is being overwritten.");
			entityTag = new NbtCompound();
			nbt.put(ENTITY_TAG, entityTag);
		}

		if (entityTag.contains(VARIANT_TAG))
			InvariablePaintings.LOGGER.warn("Existing `EntityTag.variant` is being overwritten.");
		entityTag.putString(VARIANT_TAG, variantId);

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

	@Nullable
	static public String	GetVariantId(ItemStack stack){
		NbtCompound nbt = stack.getNbt();
		if (nbt == null || !nbt.contains(ENTITY_TAG, NbtCompound.COMPOUND_TYPE))
			return null;

		NbtCompound entityTag = nbt.getCompound(ENTITY_TAG);
		if (!entityTag.contains(VARIANT_TAG, NbtCompound.STRING_TYPE))
			return null;
		
		return entityTag.getString(VARIANT_TAG);
	}

	static public boolean	HasVariantId(ItemStack stack){
		NbtCompound nbt = stack.getNbt();
		return nbt != null
		    && nbt.contains(ENTITY_TAG, NbtCompound.COMPOUND_TYPE)
		    && nbt.getCompound(ENTITY_TAG).contains(VARIANT_TAG, NbtCompound.STRING_TYPE)
		    ;
	}

}
