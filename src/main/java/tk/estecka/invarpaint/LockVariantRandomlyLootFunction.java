package tk.estecka.invarpaint;

import java.util.List;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public class LockVariantRandomlyLootFunction 
extends ConditionalLootFunction
{
	static public final Codec<LockVariantRandomlyLootFunction> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(Codecs.createStrictOptionalFieldCodec(LootConditionTypes.CODEC.listOf(), "conditions", List.of())
		                    .forGetter(conditionalLootFunction -> conditionalLootFunction.conditions))
		                    .apply(instance, LockVariantRandomlyLootFunction::new)
	);

	@Deprecated static public final Identifier OLD_ID = new Identifier("lock_variant_randomly");
	@Deprecated static public final LootFunctionType OLD_TYPE = new LootFunctionType(CODEC);

	static public final Identifier ID = new Identifier("invarpaint", "lock_variant_randomly");
	static public final LootFunctionType TYPE = new LootFunctionType(CODEC);

	static public void Register(){
		Registry.register(Registries.LOOT_FUNCTION_TYPE, ID, TYPE);
		Registry.register(Registries.LOOT_FUNCTION_TYPE, OLD_ID, OLD_TYPE);
	};

	public	LockVariantRandomlyLootFunction(List<LootCondition> conditions){
		super(conditions);
	}

	public LootFunctionType	getType(){
		return TYPE;
	}

	protected ItemStack	process(ItemStack stack, LootContext ctx){
		return PaintStackUtil.SetRandomVariant(stack, ctx.getRandom());
	}
}
