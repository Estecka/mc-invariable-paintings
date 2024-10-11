package fr.estecka.invarpaint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import fr.estecka.invarpaint.config.Command;
import fr.estecka.invarpaint.config.Config;
import fr.estecka.invarpaint.config.ConfigIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvarpaintMod
implements ModInitializer
{
	static public final String MODID = "invariablepaintings";
	static public final Logger LOGGER = LoggerFactory.getLogger("Invarpaint");

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

	static public MutableText ServersideTranslatable(String key, Object ... args){
		String fallback = Language.getInstance().get(key);
		return Text.translatableWithFallback(key, fallback, args);
	}
}
