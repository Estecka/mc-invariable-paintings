package tk.estecka.invarpaint.loot;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
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
import net.minecraft.registry.tag.PaintingVariantTags;
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
			.and(TagKey.codec(RegistryKeys.PAINTING_VARIANT).optionalFieldOf("exclusive").forGetter(a->Optional.empty()))
			.apply(instance, LockVariantRandomlyLootFunction::new)
	);

	static public final Identifier ID = new Identifier("invarpaint", "lock_variant_randomly");
	static public final LootFunctionType<LockVariantRandomlyLootFunction> TYPE = new LootFunctionType<LockVariantRandomlyLootFunction>(CODEC);

	public final TagKey<PaintingVariant> exclude;
	public final Optional<TagKey<PaintingVariant>> include;
	public final Optional<TagKey<PaintingVariant>> exclusive;

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

	static public void Register(){
		Registry.register(Registries.LOOT_FUNCTION_TYPE, ID, TYPE);
	};

	@Override
	public LootFunctionType<LockVariantRandomlyLootFunction>	getType(){
		return TYPE;
	}

	@Override
	public ItemStack	process(ItemStack stack, LootContext ctx){
		Set<Identifier> pool = new LinkedHashSet<>();
		Set<Identifier> inelligible = new HashSet<>();

		Visit(this.exclude, inelligible::add);
		if (this.exclusive.isPresent())
			Visit(exclusive.get(), inelligible::remove);

		Consumer<Identifier> doInclude = id -> {if(!inelligible.contains(id)) pool.add(id);};
		if (this.include.isPresent())
			Visit(this.include.get(), doInclude);
		else for (Identifier id : Registries.PAINTING_VARIANT.getIds())
			doInclude.accept(id);

		if (pool.size() < 0)
			return stack;

		int roll = ctx.getRandom().nextInt(pool.size());
		Identifier id = pool.toArray(new Identifier[0])[roll];
		PaintStackUtil.SetVariant(stack, id.toString());

		return stack;
	}

	static private void	Visit(TagKey<PaintingVariant> tag, Consumer<Identifier> consumer){
		for (var entry : Registries.PAINTING_VARIANT.iterateEntries(tag))
			consumer.accept(entry.getKey().get().getValue());
	}
}
