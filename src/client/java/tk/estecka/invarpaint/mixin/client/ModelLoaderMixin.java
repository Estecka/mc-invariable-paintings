package tk.estecka.invarpaint.mixin.client;

import java.util.Map;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import tk.estecka.invarpaint.InvariablePaintingsClient;

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

	@Unique
	private void FromTexture(Identifier id){
		var model = JsonUnbakedModel.deserialize(ARBITRARY_MODEL.formatted(id.toString()));
		this.unbakedModels.put(id, model);
		this.modelsToBake.put(id, model);
	}

	@Inject( method="<init>", at=@At(value="INVOKE", target="java/util/Map.values ()Ljava/util/Collection;") )
	private void	AddVariantModels(BlockColors _0, Profiler profiler, Map<?,?> _2, Map<?,?> _3, CallbackInfo ci)
	{
		profiler.swap("paintings_cit");
		this.FromTexture(InvariablePaintingsClient.CIT_FILLED);
		this.FromTexture(InvariablePaintingsClient.CIT_RANDOM);
		this.FromTexture(InvariablePaintingsClient.CIT_MISSING);

		TextureManager texManager = MinecraftClient.getInstance().getTextureManager();
		for (Identifier painting : Registries.PAINTING_VARIANT.getIds())
		{
			Identifier texture = painting.withPrefixedPath("item/paintings/");
			// if (texManager.getOrDefault(texture, null) != null)
				this.FromTexture(texture);
		}
	}
}
