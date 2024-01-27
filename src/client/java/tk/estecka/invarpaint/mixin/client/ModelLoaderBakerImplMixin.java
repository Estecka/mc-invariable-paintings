package tk.estecka.invarpaint.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.UnbakedPaintingItem;

@Mixin(targets="net.minecraft.client.render.model.ModelLoader$BakerImpl")
public class ModelLoaderBakerImplMixin 
{
	/**
	 * Unwrap the wrapper's inner JsonUnbakedModel when not falling-back.
	 * Necessary due to the Baker having a hardcoded special-case for this type.
	 */
	@ModifyExpressionValue( method="bake", at=@At(value="INVOKE", target="net/minecraft/client/render/model/ModelLoader$BakerImpl.getOrLoadModel (Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/model/UnbakedModel;") )
	private UnbakedModel PaintingModelFallforward(UnbakedModel result, Identifier id, ModelBakeSettings settings){
		if (result instanceof UnbakedPaintingItem painting && !painting.ShouldFallback())
			return painting.inner;
		else
			return result;
	}
}
