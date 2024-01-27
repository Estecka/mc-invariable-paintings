package tk.estecka.invarpaint.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin
{
	static private final String ARBITRARY_MODEL = """
		{
			"parent": "item/generated",
			"textures": {
				"layer0": "%s"
			}
		}
	""";

	@Shadow private @Final Map<Identifier, UnbakedModel> unbakedModels;
	@Shadow private @Final Map<Identifier, UnbakedModel> modelsToBake;
	@Shadow private @Final Map<Identifier, JsonUnbakedModel> jsonUnbakedModels;

	@Unique
	private JsonUnbakedModel FromTexture(Identifier id){
		return JsonUnbakedModel.deserialize(ARBITRARY_MODEL.formatted(id.toString()));
	}

	@Inject( method="<init>", at=@At(value="INVOKE", shift=Shift.BEFORE, target="java/util/Map.values ()Ljava/util/Collection;") )
	private void	AddVariantModels(BlockColors _0, Profiler profiler, Map<?,?> _2, Map<?,?> _3, CallbackInfo ci)
	{
		Identifier id = new Identifier("invarpaint", "item/missing_painting");
		var model = FromTexture(id);
		this.unbakedModels.put(id, model);
		this.modelsToBake.put(id, model);
	}
}
