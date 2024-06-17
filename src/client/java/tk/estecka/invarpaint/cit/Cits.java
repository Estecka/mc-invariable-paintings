package tk.estecka.invarpaint.cit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public class Cits
implements ClientModInitializer, ModelLoadingPlugin
{
	static public final Identifier CIT_MISSING = Identifier.of("invarpaint", "item/missing_painting");
	static public final Identifier CIT_FILLED  = Identifier.of("invarpaint", "item/filled_painting" );
	static public final Identifier CIT_RANDOM  = Identifier.of("invarpaint", "item/random_painting" );

	static public final String CIT_PREFIX = "item/painting/";

	static public final ModelIdentifier OfPainting(Identifier variantId){
		return ModelIdentifier.ofInventoryVariant(variantId.withPrefixedPath(CIT_PREFIX));
	}

	@Override
	public void onInitializeClient(){
		ModelLoadingPlugin.register(this);
	}

	@Override
	public void onInitializeModelLoader(Context pluginContext){
		pluginContext.addModels(CIT_MISSING, CIT_FILLED, CIT_RANDOM);
	}
}
