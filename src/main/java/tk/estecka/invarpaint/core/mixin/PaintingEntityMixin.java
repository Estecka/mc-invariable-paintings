package tk.estecka.invarpaint.core.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import tk.estecka.invarpaint.InvarpaintMod;
import tk.estecka.invarpaint.core.PaintStackUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

@Mixin(PaintingEntity.class)
public class PaintingEntityMixin
{
	@WrapOperation( method="onBreak", at=@At(value="INVOKE", target="Lnet/minecraft/entity/decoration/painting/PaintingEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;") )
	private ItemEntity setDropVariant(PaintingEntity painting, ItemConvertible itemType, Operation<ItemEntity> original) {
		ItemEntity entity = original.call(painting, itemType);
		ItemStack stack = entity.getStack();

		if (!stack.isOf(Items.PAINTING))
			InvarpaintMod.LOGGER.error("Unexpected painting drop type: {}", stack.getItem());
		else {
			String variantId = painting.writeNbt(new NbtCompound()).getString("variant");
			PaintStackUtil.SetVariant(stack, variantId);
		}

		return entity;
	}
}
