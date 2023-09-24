package tk.estecka.invarpaint.mixin;

import tk.estecka.invarpaint.InvariablePaintings;
import tk.estecka.invarpaint.PaintStackUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PaintingEntity.class)
public class PaintingEntityMixin
{
	@Redirect( method="onBreak", at=@At(value="INVOKE", target="Lnet/minecraft/entity/decoration/painting/PaintingEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;") )
	private ItemEntity replaceDroppedItem(PaintingEntity painting, ItemConvertible itemType) {
			if (itemType != Items.PAINTING) {
				InvariablePaintings.LOGGER.error("Unexpected painting drop type: ", itemType);
				return painting.dropItem(itemType);
			}
			else {
				String variant = painting.writeNbt(new NbtCompound()).getString("variant");
				return painting.dropStack(PaintStackUtil.CreateVariant(variant));
			}
	}
}
