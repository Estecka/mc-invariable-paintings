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
	static public final TagKey<PaintingVariant> EXCLUSIVE_TAG = TagKey.of(RegistryKeys.PAINTING_VARIANT, new Identifier("invarpaint", "exclusive"));

	static public final MapCodec<LockVariantRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec(
		instance -> ConditionalLootFunction.addConditionsField(instance)
			.and(TagKey.codec(RegistryKeys.PAINTING_VARIANT).optionalFieldOf("exclude", EXCLUSIVE_TAG).forGetter(f->f.exclude))
			.and(TagKey.codec(RegistryKeys.PAINTING_VARIANT).optionalFieldOf("include").forGetter(f->f.include))
			.and(TagKey.codec(RegistryKeys.PAINTING_VARIANT).optionalFieldOf("exclusive").forGetter(f->f.exclusive))
			.apply(instance, LockVariantRandomlyLootFunction::new)
	);

	static public final Identifier ID = new Identifier("invarpaint", "lock_variant_randomly");
	static public final LootFunctionType<LockVariantRandomlyLootFunction> TYPE = new LootFunctionType<LockVariantRandomlyLootFunction>(CODEC);

	static public void Register(){
		Registry.register(Registries.LOOT_FUNCTION_TYPE, ID, TYPE);
	};


	private final TagKey<PaintingVariant> exclude;
	private final Optional<TagKey<PaintingVariant>> include;
	private final Optional<TagKey<PaintingVariant>> exclusive;

	private LockVariantRandomlyLootFunction(
		List<LootCondition> conditions,
		TagKey<PaintingVariant> exclude,
		Optional<TagKey<PaintingVariant>> include,
		Optional<TagKey<PaintingVariant>> exclusive
	){
		super(conditions);
		this.include = include;
		this.exclude = exclude;
		this.exclusive = exclusive;
	}

	@Override
	public LootFunctionType<LockVariantRandomlyLootFunction>	getType(){
		return TYPE;
	}

	@Override
	public ItemStack	process(ItemStack stack, LootContext ctx){
		VariantPool pool  = new VariantPool();
		pool.Add(this.exclude);

		if (include.isPresent())
			pool.RemoveFrom(include.get());
		else
			pool.Invert();

		if (exclusive.isPresent())
			pool.Add(exclusive.get());

		Identifier variant = pool.GetRandom(ctx.getRandom());
		if (variant != null)
			PaintStackUtil.SetVariant(stack, variant.toString());

		return stack;
	}

}
