package tk.estecka.invarpaint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import tk.estecka.invarpaint.crafting.FilledPaintingRecipe;
import tk.estecka.invarpaint.crafting.PaintingReplicationRecipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvariablePaintings implements ModInitializer
{
	static public final Logger LOGGER = LoggerFactory.getLogger("Invar-Paint");

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
