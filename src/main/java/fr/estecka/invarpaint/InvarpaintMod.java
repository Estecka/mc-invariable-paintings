package fr.estecka.invarpaint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import fr.estecka.invarpaint.config.Command;
import fr.estecka.invarpaint.config.Config;
import fr.estecka.invarpaint.config.ConfigIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvarpaintMod
implements ModInitializer
{
	static public final String MODID = "invarpaint";
	static public final Logger LOGGER = LoggerFactory.getLogger(MODID);

	static public final ConfigIO IO = new ConfigIO("invarpaint.properties");
	static public final Config CONFIG = new Config();

	@Override
	public void onInitialize(){
		IO.TryGetIfExists(CONFIG);
		Command.Register();
	}

	static public boolean IsNokebabInstalled(){
		return FabricLoader.getInstance().isModLoaded("no-kebab");
	}
}
