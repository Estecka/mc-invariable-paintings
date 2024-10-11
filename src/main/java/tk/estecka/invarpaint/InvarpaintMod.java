package tk.estecka.invarpaint;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import tk.estecka.invarpaint.core.config.Config;
import tk.estecka.invarpaint.core.config.ConfigIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvarpaintMod
{
	static public final String MODID = "invariablepaintings";
	static public final Logger LOGGER = LoggerFactory.getLogger("Invarpaint");

	static public final ConfigIO IO = new ConfigIO("invarpaint.properties");
	static public final Config CONFIG = new Config();
	static {
		if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT))
			IO.TryGetIfExists(CONFIG);
		else
			IO.TryGetOrCreate(CONFIG);
	}

	static public boolean IsNokebabInstalled(){
		return FabricLoader.getInstance().isModLoaded("no-kebab");
	}

	static public MutableText ServersideTranslatable(String key, Object ... args){
		String fallback = Language.getInstance().get(key);
		return Text.translatableWithFallback(key, fallback, args);
	}
}
