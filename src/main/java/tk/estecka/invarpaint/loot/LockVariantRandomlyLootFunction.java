package tk.estecka.invarpaint.loot;

import java.util.List;
import java.util.Optional;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import tk.estecka.invarpaint.core.PaintStackUtil;

public class LockVariantRandomlyLootFunction
extends ConditionalLootFunction
{
	static public final TagKey<PaintingVariant> EXCLUSIVE_TAG = TagKey.of(RegistryKeys.PAINTING_VARIANT, new Identifier("invarpaint", "exclusive"));

	static public final MapCodec<LockVariantRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec(
		instance -> ConditionalLootFunction.addConditionsField(instance)
			.and(PoolIdentifier.CODEC.listOf().optionalFieldOf("variants").forGetter(f->f.variants))
			.apply(instance, LockVariantRandomlyLootFunction::new)
	);

	static public final Identifier ID = new Identifier("invarpaint", "lock_variant_randomly");
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
		Random random = ctx.getRandom();
		
		if (this.variants.isEmpty())
			return PaintStackUtil.SetRandomVariant(stack, random);
		
		Identifier variant = PoolIdentifier.GetRandom(PoolIdentifier.Combine(this.variants.get()), random);
		if (variant != null)
			PaintStackUtil.SetVariant(stack, variant.toString());

		return stack;
	}

}
