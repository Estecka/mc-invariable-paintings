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
	static public final Codec<LockVariantRandomlyLootFunction> CODEC;

	static {
		CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(Codecs.createStrictOptionalFieldCodec(LootConditionTypes.CODEC.listOf(), "conditions", List.of()).forGetter(conditionalLootFunction -> conditionalLootFunction.conditions)).apply(instance, LockVariantRandomlyLootFunction::new);
		});
	}

	static public final LootFunctionType TYPE = Registry.register(
			Registries.LOOT_FUNCTION_TYPE,
			new Identifier("lock_variant_randomly"),
			new LootFunctionType(CODEC)
		);

	static public void RegisterFunction(){
		// Loads the class and initializes static variables
	};

	public	LockVariantRandomlyLootFunction(List<LootCondition> conditions){
		super(conditions);
	}

	public LootFunctionType	getType(){
		return TYPE;
	}

	protected ItemStack	process(ItemStack stack, LootContext ctx){
		return PaintStackCreator.SetRandomVariant(stack, ctx.getRandom());
	}
}
