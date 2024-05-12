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
import tk.estecka.invarpaint.core.PaintStackUtil;

public class LockVariantRandomlyLootFunction
extends ConditionalLootFunction
{
	static public final MapCodec<LockVariantRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec(
		instance -> ConditionalLootFunction.addConditionsField(instance)
			.and(TagKey.codec(RegistryKeys.PAINTING_VARIANT).optionalFieldOf("include").forGetter(a->Optional.empty()))
			.and(TagKey.codec(RegistryKeys.PAINTING_VARIANT).optionalFieldOf("exclude").forGetter(a->Optional.empty()))
			.apply(instance, LockVariantRandomlyLootFunction::new)
	);

	static public final Identifier ID = new Identifier("invarpaint", "lock_variant_randomly");
	static public final LootFunctionType<LockVariantRandomlyLootFunction> TYPE = new LootFunctionType<LockVariantRandomlyLootFunction>(CODEC);

	private final Optional<TagKey<PaintingVariant>> include;
	private final Optional<TagKey<PaintingVariant>> exclude;

	public	LockVariantRandomlyLootFunction(List<LootCondition> conditions, Optional<TagKey<PaintingVariant>> include, Optional<TagKey<PaintingVariant>> exclude){
		super(conditions);
		this.include = include;
		this.exclude = exclude;
	}

	static public void Register(){
		Registry.register(Registries.LOOT_FUNCTION_TYPE, ID, TYPE);
	};


	@Override
	public LootFunctionType<LockVariantRandomlyLootFunction>	getType(){
		return TYPE;
	}

	@Override
	public ItemStack	process(ItemStack stack, LootContext ctx){
		if (!include.isPresent())
			return PaintStackUtil.SetRandomVariant(stack, ctx.getRandom());

		var optResult = Registries.PAINTING_VARIANT.getRandomEntry(include.get(), ctx.getRandom());
		if (optResult.isPresent())
			PaintStackUtil.SetVariant(stack, optResult.get().getKey().get().getValue().toString());

		return stack;
	}
}
