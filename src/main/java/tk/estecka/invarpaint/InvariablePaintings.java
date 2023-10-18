package tk.estecka.invarpaint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.crafting.FilledPaintingRecipe;
import tk.estecka.invarpaint.crafting.PaintingReplicationRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvariablePaintings implements ModInitializer
{
	static public final String MODID = "invariablepaintings";
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

		RegisterPack("looting", "Painting Loot", true);

		RegisterPack("crafting-unbound"  , "All Painting Recipes"      , false);
		RegisterPack("crafting-cloning"  , "Painting Recipe: Cloning"  , false);
		RegisterPack("crafting-expensive", "Painting Recipe: Expensive", false);
		RegisterPack("crafting-iterative", "Painting Recipe: Iterative", false);
		RegisterPack("crafting-recycling", "Painting Recipe: Recycling", false);
	}

	void RegisterPack(String id, String displayName, boolean defaultEnabled){
		final var mod = FabricLoader.getInstance().getModContainer(MODID).get();
		ResourceManagerHelper.registerBuiltinResourcePack(
			new Identifier(MODID, id), mod,
			Text.literal(displayName),
			defaultEnabled ? ResourcePackActivationType.DEFAULT_ENABLED : ResourcePackActivationType.NORMAL
		);
	}

}
