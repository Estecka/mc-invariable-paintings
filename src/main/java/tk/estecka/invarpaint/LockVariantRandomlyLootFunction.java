package tk.estecka.invarpaint;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
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
	static public class Serializer
	extends ConditionalLootFunction.Serializer<LockVariantRandomlyLootFunction>
	{
		@Override
		public LockVariantRandomlyLootFunction	fromJson(JsonObject json, JsonDeserializationContext ctx, LootCondition[] conditions){
			return new LockVariantRandomlyLootFunction(conditions);
		}
	}

	static public final Identifier ID = new Identifier("lock_variant_randomly");
	static public final LootFunctionType TYPE = new LootFunctionType(new LockVariantRandomlyLootFunction.Serializer());

	static public void Register(){
		Registry.register(Registries.LOOT_FUNCTION_TYPE, ID, TYPE);
	};

	public	LockVariantRandomlyLootFunction(LootCondition[] conditions){
		super(conditions);
	}

	public LootFunctionType	getType(){
		return TYPE;
	}

	protected ItemStack	process(ItemStack stack, LootContext ctx){
		return PaintStackUtil.SetRandomVariant(stack, ctx.getRandom());
	}
}
