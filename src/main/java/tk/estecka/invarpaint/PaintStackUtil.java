package tk.estecka.invarpaint;

import org.jetbrains.annotations.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;

public class PaintStackUtil
{
	static public final String OBFUSCATED_TAG = "obfuscated";
	static public final String ENTITY_TAG = "EntityTag";
	static public final String VARIANT_TAG = "variant";
	static public final String DYES_TAG = "dyeCode";

	static public ItemStack	SetVariant(ItemStack stack, String variantId){
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

	static public ItemStack SetDyeCode(ItemStack stack, long dyeCode){
		NbtCompound nbt = stack.getOrCreateNbt();

		if (nbt.contains(VARIANT_TAG))
			InvariablePaintings.LOGGER.warn("Existing `dyeCode` is being overwritten.");
		nbt.putLong(DYES_TAG, dyeCode);

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

	static public ItemStack	CreateDyeCode(long dyeCode){
		return SetDyeCode(new ItemStack(Items.PAINTING), dyeCode);
	}

	static public boolean	IsBlank(ItemStack stack){
		return !HasDyeCode(stack) && !HasVariantId(stack);
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

	static public long	GetDyeCode(ItemStack stack){
		NbtCompound nbt = stack.getNbt();
		return (nbt != null) ? nbt.getLong(DYES_TAG) : 0L;
	}

	static public boolean	HasDyeCode(ItemStack stack){
		NbtCompound nbt = stack.getNbt();
		return (nbt != null) ? nbt.contains(DYES_TAG, NbtElement.LONG_TYPE) : false;
	}

	static public boolean	IsObfuscated(ItemStack stack){
		NbtCompound nbt = stack.getNbt();
		return (nbt != null) ? nbt.contains(OBFUSCATED_TAG) : false;
	}

	static public ItemStack Obfuscate(ItemStack stack){
		NbtList lore = new NbtList();
		lore.add(NbtString.of(Text.Serializer.toJson(Text.translatable("painting.obfuscated"))));

		NbtCompound display = new NbtCompound();
		display.put(ItemStack.LORE_KEY, lore);
		
		NbtCompound nbt = new NbtCompound();
		nbt.putBoolean(OBFUSCATED_TAG, true);
		nbt.put(ItemStack.DISPLAY_KEY, display);

		stack = stack.copy();
		stack.setNbt(nbt);
		return stack;
	}

}
