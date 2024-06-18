package tk.estecka.invarpaint.cit;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin.DataLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.InvarpaintMod;

public class Cits
implements ClientModInitializer, PreparableModelLoadingPlugin<Set<Identifier>>, DataLoader<Set<Identifier>>
{
	static public final Identifier CIT_MISSING = Identifier.of("invarpaint", "item/missing_painting");
	static public final Identifier CIT_FILLED  = Identifier.of("invarpaint", "item/filled_painting" );
	static public final Identifier CIT_RANDOM  = Identifier.of("invarpaint", "item/random_painting" );

	static public final String CIT_PREFIX = "item/painting/";

	static private final Set<Identifier> availableCitsVariants = new HashSet<>();

	static public ModelIdentifier OfPainting(Identifier variantId){
		return ModelIdentifier.ofInventoryVariant(variantId.withPrefixedPath(CIT_PREFIX));
	}

	static public Set<Identifier> GetAvailableCitVariants(){
		return Set.copyOf(availableCitsVariants);
	}

	@Override
	public void onInitializeClient(){
		PreparableModelLoadingPlugin.register(this, this);
	}

	@Override
	public void onInitializeModelLoader(Set<Identifier> modelIds, ModelLoadingPlugin.Context pluginContext){
		pluginContext.addModels(CIT_MISSING, CIT_FILLED, CIT_RANDOM);
		availableCitsVariants.clear();
		availableCitsVariants.addAll(modelIds);
		InvarpaintMod.LOGGER.info("Found {} painting CITs", modelIds.size());
	}

	@Override
	public CompletableFuture<Set<Identifier>> load(ResourceManager manager, Executor executor){
		return CompletableFuture.supplyAsync(()->FindCITs(manager), executor);
	}

	static private Set<Identifier> FindCITs(ResourceManager manager){
		Set<Identifier> modelIds = new HashSet<>();

		for (Identifier id : manager.findResources("textures/item/painting", id -> id.getPath().endsWith(".png")).keySet()) {
			String path = id.getPath();
			path = path.substring("textures/".length(), path.length()-".png".length());
			modelIds.add(Identifier.of(id.getNamespace(), path));
		}

		return modelIds;
	}
}
