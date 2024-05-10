package tk.estecka.invarpaint;

import java.util.List;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LockVariantRandomlyLootFunction 
extends ConditionalLootFunction
{
	static public final MapCodec<LockVariantRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec(
		instance -> ConditionalLootFunction.addConditionsField(instance).apply(instance, LockVariantRandomlyLootFunction::new)
	);

	static public final Identifier ID = new Identifier("invarpaint", "lock_variant_randomly");
	static public final LootFunctionType<LockVariantRandomlyLootFunction> TYPE = new LootFunctionType<LockVariantRandomlyLootFunction>(CODEC);

	static public void Register(){
		Registry.register(Registries.LOOT_FUNCTION_TYPE, ID, TYPE);
	};

	public	LockVariantRandomlyLootFunction(List<LootCondition> conditions){
		super(conditions);
	}

	@Override
	public LootFunctionType<LockVariantRandomlyLootFunction>	getType(){
		return TYPE;
	}

	@Override
	public ItemStack	process(ItemStack stack, LootContext ctx){
		return PaintStackUtil.SetRandomVariant(stack, ctx.getRandom());
	}
}
