package tk.estecka.invarpaint;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import static tk.estecka.invarpaint.InvariablePaintings.MODID;

public class InvariablePaintingsClient
implements ClientModInitializer
{
	static public final Identifier CIT_MISSING = new Identifier("invarpaint", "item/missing_painting");
	static public final Identifier CIT_FILLED  = new Identifier("invarpaint", "item/filled_painting");
	static public final Identifier CIT_RANDOM  = new Identifier("invarpaint", "item/random_painting");
	static public final String CIT_PREFIX = "item/painting/";

	public void	onInitializeClient(){
		var mod = FabricLoader.getInstance().getModContainer(MODID).get();
		ResourceManagerHelper.registerBuiltinResourcePack(
			new Identifier(MODID, "vanilla-cit"),
			mod,
			Text.literal("Vanilla Paintings CITs"),
			ResourcePackActivationType.DEFAULT_ENABLED
		);
	}
}
