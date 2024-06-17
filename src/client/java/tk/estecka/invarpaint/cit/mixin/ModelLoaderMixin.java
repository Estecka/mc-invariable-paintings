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

	@Shadow private void loadInventoryVariantItemModel(Identifier id) { throw new AssertionError(); }

	@Unique
	private void AddUnsourced(Identifier id, @Nullable Identifier fallback){
		var model = new UnbakedPaintingItem(id, fallback);
		this.unbakedModels.put(id, model);
		this.modelsToBake.put(ModelIdentifier.ofInventoryVariant(id), model);
	}

	@Inject( method="<init>", at=@At(value="INVOKE", target="java/util/Map.values ()Ljava/util/Collection;") )
	private void	AddVariantModels(BlockColors _0, Profiler profiler, Map<?,?> _2, Map<?,?> _3, CallbackInfo ci)
	{
		profiler.swap("painting_items");
		this.loadInventoryVariantItemModel(Cits.CIT_FILLED .id());
		this.loadInventoryVariantItemModel(Cits.CIT_RANDOM .id());
		this.loadInventoryVariantItemModel(Cits.CIT_MISSING.id());

		var registry = RegistryUtil.GetPaintingRegitry();
		if (!registry.isPresent())
			InvarpaintMod.LOGGER.warn("Resources were reloaded with no registry; CITs were not generated.");
		else for (Identifier painting : registry.get().getIds())
		{
			Identifier texture = painting.withPrefixedPath(Cits.CIT_PREFIX);
			this.AddUnsourced(texture, Cits.CIT_FILLED.id().withPrefixedPath("item/"));
		}
	}
}
