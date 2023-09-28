package tk.estecka.invarpaint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import tk.estecka.invarpaint.crafting.FilledPaintingRecipe;
import tk.estecka.invarpaint.crafting.PaintingDeobfuscationRecipe;
import tk.estecka.invarpaint.crafting.PaintingReplicationRecipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvariablePaintings implements ModInitializer
{
	static public final Logger LOGGER = LoggerFactory.getLogger("Invar-Paint");

	static public final Text CATEGORY_NAME = Text.translatable("gamerule.category.invarpaint").formatted(Formatting.BOLD, Formatting.YELLOW);
	static public final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(new Identifier("invarpaint", "general"), CATEGORY_NAME);

	static public final GameRules.Key<BooleanRule> CREATING_RULE  = GameRuleRegistry.register("invarpaint.allowCreation",      CATEGORY, GameRuleFactory.createBooleanRule(false));
	static public final GameRules.Key<BooleanRule> DERIVATE_RULE  = GameRuleRegistry.register("invarpaint.allowDerivation",    CATEGORY, GameRuleFactory.createBooleanRule(false));
	static public final GameRules.Key<BooleanRule> REPLICA_RULE   = GameRuleRegistry.register("invarpaint.allowReplication",   CATEGORY, GameRuleFactory.createBooleanRule(false));
	static public final GameRules.Key<BooleanRule> OBFUSCATE_RULE = GameRuleRegistry.register("invarpaint.obfuscatedCrafting", CATEGORY, GameRuleFactory.createBooleanRule(true ));

	static public boolean IsNokebabInstalled(){
		return FabricLoader.getInstance().isModLoaded("no-kebab");
	}

	@Override
	public void onInitialize() {
		SellPaintingFactory.Register();
		LockVariantRandomlyLootFunction.Register();
		FilledPaintingRecipe.Register();
		PaintingReplicationRecipe.Register();
		PaintingDeobfuscationRecipe.Register();
	}

}
