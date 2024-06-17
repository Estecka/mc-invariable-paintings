package tk.estecka.invarpaint.cit;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class UnbakedPaintingItem
implements UnbakedModel
{
	static private final String ARBITRARY_MODEL = """
		{
			"parent": "item/generated",
			"textures": {
				"layer0": "%s"
			}
		}
	""";
	
	private final Identifier modelId;
	private final Identifier textureId;
	private final @Nullable Identifier fallbackId;
	public final JsonUnbakedModel inner;
	
	static public JsonUnbakedModel CreateJson(Identifier id){
		return JsonUnbakedModel.deserialize(ARBITRARY_MODEL.formatted(id.toString()));
	}

	public UnbakedPaintingItem(Identifier id, @Nullable Identifier fallbackId){
		this.modelId = id;
		this.textureId = modelId.withPrefixedPath("textures/").withSuffixedPath(".png");
		this.fallbackId = fallbackId;
		this.inner = CreateJson(modelId);
	}

	@Override
	public Collection<Identifier> getModelDependencies(){
		var deps = List.copyOf(inner.getModelDependencies());
		if (fallbackId != null)
			deps.add(fallbackId);
		return deps;
	}

	@Override
	public void	setParents(Function<Identifier, UnbakedModel> modelLoader){
		inner.setParents(modelLoader);
	}

	@Override
	public @Nullable BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> spriteGetter, ModelBakeSettings settings){
		if (!this.ShouldFallback()) 
			return baker.bake(this.modelId, settings);
		else if (this.fallbackId != null)
			return baker.bake(this.fallbackId, settings);
		else
			throw new RuntimeException("Attempted to bake a painting model with no fallback: "+this.modelId.toString());
	}

	public boolean	ShouldFallback(){
		return this.fallbackId != null
		    && MinecraftClient.getInstance().getResourceManager().getResource(this.textureId).isEmpty()
		    ;
	}
}
