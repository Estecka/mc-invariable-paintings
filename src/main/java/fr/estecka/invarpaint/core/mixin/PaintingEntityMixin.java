package fr.estecka.invarpaint.core.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import fr.estecka.invarpaint.api.PaintStackUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

@Mixin(PaintingEntity.class)
public class PaintingEntityMixin
{
	@ModifyExpressionValue( method="onBreak", at=@At(value="INVOKE", target="Lnet/minecraft/entity/decoration/painting/PaintingEntity;dropItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;") )
	private ItemEntity setDropVariant(ItemEntity original) {
		ItemStack stack = original.getStack();

		if (stack.isOf(Items.PAINTING))
			PaintStackUtil.SetVariant(stack, (PaintingEntity)(Object)this);

		return original;
	}
}
