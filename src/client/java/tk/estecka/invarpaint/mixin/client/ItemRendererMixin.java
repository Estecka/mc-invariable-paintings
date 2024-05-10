package tk.estecka.invarpaint.mixin.client;

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
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.InvariablePaintingsClient;
import tk.estecka.invarpaint.PaintStackUtil;

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
			String variant = PaintStackUtil.GetVariantId(stack);
			if (variant != null)
			{
				Identifier variantId = Identifier.tryParse(variant);
				if (variantId == null || !Registries.PAINTING_VARIANT.containsId(variantId))
					return modelManager.getModel(InvariablePaintingsClient.CIT_MISSING);
				else {
					var model = modelManager.getModel(variantId.withPrefixedPath(InvariablePaintingsClient.CIT_PREFIX));
					if (model != null)
						return model;
					else
						return modelManager.getModel(InvariablePaintingsClient.CIT_FILLED);
				}
			}
		}

		return original.call(instance, stack);
	}
}
