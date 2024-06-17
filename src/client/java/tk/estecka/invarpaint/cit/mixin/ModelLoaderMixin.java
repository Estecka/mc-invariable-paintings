package tk.estecka.invarpaint.cit.mixin;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import tk.estecka.invarpaint.InvarpaintMod;
import tk.estecka.invarpaint.RegistryUtil;
import tk.estecka.invarpaint.cit.Cits;
import tk.estecka.invarpaint.cit.UnbakedPaintingItem;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin
{
	@Shadow private @Final Map<Identifier, UnbakedModel> unbakedModels;
	@Shadow private @Final Map<ModelIdentifier, UnbakedModel> modelsToBake;

	@Unique
	private void AddUnsourced(Identifier texId, @Nullable Identifier fallback){
		var model = new UnbakedPaintingItem(texId, fallback);
		this.unbakedModels.put(texId, model);
		this.modelsToBake.put(ModelIdentifier.ofInventoryVariant(texId), model);
	}

	@Inject( method="<init>", at=@At(value="INVOKE", target="java/util/Map.values ()Ljava/util/Collection;") )
	private void	AddVariantModels(BlockColors _0, Profiler profiler, Map<?,?> _2, Map<?,?> _3, CallbackInfo ci)
	{
		profiler.swap("painting_items");
		this.AddUnsourced(Cits.CIT_FILLED, null);
		this.AddUnsourced(Cits.CIT_RANDOM, null);
		this.AddUnsourced(Cits.CIT_MISSING, null);

		var registry = RegistryUtil.GetPaintingRegitry();
		if (!registry.isPresent())
			InvarpaintMod.LOGGER.warn("Resources were reloaded with no registry; CITs were not generated.");
		else for (Identifier painting : registry.get().getIds())
		{
			Identifier texture = painting.withPrefixedPath(Cits.CIT_PREFIX);
			this.AddUnsourced(texture, Cits.CIT_FILLED);
		}
	}
}
