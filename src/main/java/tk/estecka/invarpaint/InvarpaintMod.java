package tk.estecka.invarpaint;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvarpaintMod
{
	static public final String MODID = "invariablepaintings";
	static public final Logger LOGGER = LoggerFactory.getLogger("Invar-Paint");

	static public boolean IsNokebabInstalled(){
		return FabricLoader.getInstance().isModLoaded("no-kebab");
	}
}
