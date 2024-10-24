package fr.estecka.invarpaint.loot;

import java.util.List;
import java.util.Optional;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import fr.estecka.invarpaint.api.PaintStackUtil;

public class LockVariantRandomlyLootFunction
extends ConditionalLootFunction
{
	static public final MapCodec<LockVariantRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec(
		instance -> ConditionalLootFunction.addConditionsField(instance)
			.and(PoolIdentifier.CODEC.listOf().optionalFieldOf("variants").forGetter(f->f.variants))
			.apply(instance, LockVariantRandomlyLootFunction::new)
	);

	static public final Identifier ID = Identifier.of("invarpaint", "lock_variant_randomly");
	static public final LootFunctionType<LockVariantRandomlyLootFunction> TYPE = new LootFunctionType<LockVariantRandomlyLootFunction>(CODEC);

	static public void Register(){
		Registry.register(Registries.LOOT_FUNCTION_TYPE, ID, TYPE);
	};


	private final Optional<List<PoolIdentifier>> variants;

	private LockVariantRandomlyLootFunction(List<LootCondition> conditions, Optional<List<PoolIdentifier>> variants){
		super(conditions);
		this.variants = variants;
	}

	@Override
	public LootFunctionType<LockVariantRandomlyLootFunction>	getType(){
		return TYPE;
	}

	@Override
	public ItemStack	process(ItemStack stack, LootContext ctx){
		var registry  = ctx.getWorld().getRegistryManager().getOrThrow(RegistryKeys.PAINTING_VARIANT);
		Random random = ctx.getRandom();
		Identifier variantId = null;

		if (this.variants.isPresent())
			variantId = PoolIdentifier.GetRandom(this.variants.get(), random, registry);
		else {
			var entry = registry.getRandom(random);
			if (entry.isPresent())
				variantId = entry.get().getKey().get().getValue();
		}

		if (variantId != null)
			PaintStackUtil.SetVariant(stack, variantId);

		return stack;
	}

}
