package fr.estecka.invarpaint.loot;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.estecka.invarpaint.api.PaintStackUtil;

public class LockVariantRandomlyLootFunction
extends LootItemConditionalFunction
{
	static public final MapCodec<LockVariantRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec(
		instance -> LootItemConditionalFunction.commonFields(instance)
			.and(PoolIdentifier.CODEC.listOf().optionalFieldOf("variants").forGetter(f->f.variants))
			.apply(instance, LockVariantRandomlyLootFunction::new)
	);

	static public final Identifier ID = Identifier.fromNamespaceAndPath("invarpaint", "lock_variant_randomly");

	static public void Register(){
		Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, ID, CODEC);
	};


	private final Optional<List<PoolIdentifier>> variants;

	private LockVariantRandomlyLootFunction(List<LootItemCondition> conditions, Optional<List<PoolIdentifier>> variants){
		super(conditions);
		this.variants = variants;
	}

	@Override
	public MapCodec<? extends LootItemConditionalFunction> codec() {
		return CODEC;
	}

	@Override
	public ItemStack run(ItemStack stack, LootContext ctx){
		var registry  = ctx.getLevel().registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT);
		RandomSource random = ctx.getRandom();
		Holder<PaintingVariant> variantEntry = null;

		if (this.variants.isPresent())
			variantEntry = PoolIdentifier.GetRandom(this.variants.get(), random, registry);
		else
			variantEntry = registry.getRandom(random).orElse(null);

		if (variantEntry != null)
			PaintStackUtil.SetVariant(stack, variantEntry);

		return stack;
	}

}
