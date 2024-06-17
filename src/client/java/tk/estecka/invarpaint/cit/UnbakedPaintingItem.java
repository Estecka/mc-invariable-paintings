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
	
	private final Identifier texture;
	private final @Nullable Identifier fallback;
	public final JsonUnbakedModel inner;
	
	static public JsonUnbakedModel CreateJson(Identifier id){
		return JsonUnbakedModel.deserialize(ARBITRARY_MODEL.formatted(id.toString()));
	}

	public UnbakedPaintingItem(Identifier textureId, @Nullable Identifier fallbackId){
		this.texture = textureId.withPrefixedPath("textures/").withSuffixedPath(".png");
		this.fallback = fallbackId;
		this.inner = CreateJson(textureId);
	}

	@Override
	public Collection<Identifier> getModelDependencies(){
		var deps = List.copyOf(inner.getModelDependencies());
		if (fallback != null)
			deps.add(fallback);
		return deps;
	}

	@Override
	public void	setParents(Function<Identifier, UnbakedModel> modelLoader){
		inner.setParents(modelLoader);
	}

	@Override 
	public @Nullable BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> spriteGetter, ModelBakeSettings settings){
		if (!this.ShouldFallback()) 
			return baker.bake(this.texture, settings);
		else if (this.fallback != null)
			return baker.bake(this.fallback, settings);
		else
			throw  new RuntimeException("Attempted to bake a painting model with no fallback: "+this.texture.toString());
	}

	public boolean	ShouldFallback(){
		return this.fallback != null
			&& MinecraftClient.getInstance().getResourceManager().getResource(texture).isEmpty()
		    ;
	}
}
