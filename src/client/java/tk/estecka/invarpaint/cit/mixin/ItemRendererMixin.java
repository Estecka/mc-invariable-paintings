package tk.estecka.invarpaint.cit.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.RegistryUtil;
import tk.estecka.invarpaint.cit.Cits;
import tk.estecka.invarpaint.core.PaintStackUtil;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{
	@Shadow private @Final ItemModels models;

	@WrapOperation( method="getModel", at=@At( value="INVOKE", target="net/minecraft/client/render/item/ItemModels.getModel (Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;") )
	private BakedModel	GetPaintingModel(ItemModels instance, ItemStack stack, Operation<BakedModel> original)
	{
		final BakedModelManager modelManager = this.models.getModelManager();

		if (stack.isOf(Items.PAINTING))
		{
			if (PaintStackUtil.IsObfuscated(stack))
				return modelManager.getModel(Cits.CIT_RANDOM);

			String variantName = PaintStackUtil.GetVariantId(stack);
			if (variantName != null)
			{
				Identifier variantId = Identifier.tryParse(variantName);
				var registry = RegistryUtil.GetPaintingRegitry();
				if (variantId == null || (registry.isPresent() && !registry.get().containsId(variantId)))
					return modelManager.getModel(Cits.CIT_MISSING);
				else {
					var model = modelManager.getModel(variantId.withPrefixedPath(Cits.CIT_PREFIX));
					if (model != null)
						return model;
					else
						return modelManager.getModel(Cits.CIT_FILLED);
				}
			}
		}

		return original.call(instance, stack);
	}
}
