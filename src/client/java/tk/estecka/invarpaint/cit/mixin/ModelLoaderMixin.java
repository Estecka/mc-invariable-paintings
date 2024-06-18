package tk.estecka.invarpaint.cit.mixin;

import java.util.Map;
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
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import tk.estecka.invarpaint.InvarpaintMod;
import tk.estecka.invarpaint.cit.Cits;
import tk.estecka.invarpaint.cit.UnbakedPaintingItem;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin
{
	@Shadow private @Final Map<Identifier, UnbakedModel> unbakedModels;
	@Shadow private @Final Map<ModelIdentifier, UnbakedModel> modelsToBake;

	@Unique
	private void AddFromTexture(Identifier modelId) {
		JsonUnbakedModel model = UnbakedPaintingItem.CreateJson(modelId);
		this.unbakedModels.put(modelId, model);
		this.modelsToBake.put(ModelIdentifier.ofInventoryVariant(modelId), model);
	}

	/**
	 * Injected right before the post-processing of `modelsToBake`.
	 */
	@Inject( method="<init>", at=@At(value="INVOKE", target="java/util/Map.values ()Ljava/util/Collection;") )
	private void	AddVariantModels(BlockColors _0, Profiler profiler, Map<?,?> _2, Map<?,?> _3, CallbackInfo ci)
	{
		profiler.swap("painting_items");
		InvarpaintMod.LOGGER.info("Creating painting models from CITs...");
		for (Identifier paintingId : Cits.GetAvailableCitVariants())
			this.AddFromTexture(paintingId);
	}
}
