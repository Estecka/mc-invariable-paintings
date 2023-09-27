package tk.estecka.invarpaint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import tk.estecka.invarpaint.crafting.FilledPaintingRecipe;
import tk.estecka.invarpaint.crafting.PaintingReplicationRecipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvariablePaintings implements ModInitializer
{
	static public final Logger LOGGER = LoggerFactory.getLogger("Invar-Paint");

	static public final GameRules.Key<BooleanRule> CRAFTING_RULE = GameRuleRegistry.register("invarpaint.allowCrafing",     GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));
	static public final GameRules.Key<BooleanRule> REPLICA_RULE  = GameRuleRegistry.register("invarpaint.allowReplication", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));

	static public boolean IsNokebabInstalled(){
		return FabricLoader.getInstance().isModLoaded("no-kebab");
	}

	@Override
	public void onInitialize() {
		SellPaintingFactory.Register();
		LockVariantRandomlyLootFunction.Register();
		FilledPaintingRecipe.Register();
		PaintingReplicationRecipe.Register();
	}

}
