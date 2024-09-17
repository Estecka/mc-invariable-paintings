package fr.estecka.invarpaint.pick.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import fr.estecka.invarpaint.InvarpaintMod;
import fr.estecka.invarpaint.api.PaintStackUtil;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@WrapOperation( method="doItemPick", at=@At(value="INVOKE", target="net/minecraft/entity/Entity.getPickBlockStack ()Lnet/minecraft/item/ItemStack;") )
	private ItemStack	PickPaintingVariant(Entity entity, Operation<ItemStack> original){
		ItemStack stack = original.call(entity);

		if (entity instanceof PaintingEntity && Screen.hasControlDown()){
			if (!stack.isOf(Items.PAINTING))
				InvarpaintMod.LOGGER.error("Unexpected painting drop type: {}", stack.getItem());
			else
				PaintStackUtil.SetVariant(stack, entity);
		}

		return stack;
	}
}
