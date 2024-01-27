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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{
	@Shadow private @Final ItemModels models;

	Identifier test = new Identifier("invarpaint", "item/missing_painting");

	@WrapOperation( method="getModel", at=@At( value="INVOKE", target="net/minecraft/client/render/item/ItemModels.getModel (Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;") )
	private BakedModel	GetPaintingModel(ItemModels instance, ItemStack stack, Operation<BakedModel> original)
	{
		BakedModel model = null;

		if (stack.isOf(Items.PAINTING)){
			model = models.getModelManager().getModel(test);
		}

		if (model != null)
			return model;

		return original.call(instance, stack);
	}
}
