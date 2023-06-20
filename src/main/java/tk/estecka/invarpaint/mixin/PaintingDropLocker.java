package tk.estecka.invarpaint.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.estecka.invarpaint.InvariablePaintings;
import tk.estecka.invarpaint.PaintStackCreator;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(PaintingEntity.class)
public class PaintingDropLocker {
	@Redirect(
			method = "onBreak",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/decoration/painting/PaintingEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
			)
	)
	private ItemEntity replaceDroppedItem(PaintingEntity painting, ItemConvertible itemType) {
		// InvariablePaintings.LOGGER.debug("Painting drop is being unrandomized!");
		if (itemType != Items.PAINTING) {
			InvariablePaintings.LOGGER.error("Unexpected painting drop type: ", itemType);
			return painting.dropItem(itemType);
		} else {
			String variant = painting.writeNbt(new NbtCompound()).getString("variant");
			return painting.dropStack(PaintStackCreator.CreateVariant(variant));
		}
	}

	/*
	@Inject(
			method = "placePainting(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Ljava/util/Optional;",
			at = @At(
					value = "TAIL"
			),
			cancellable = true)
	private static void placePainting(World world, BlockPos pos, Direction facing, CallbackInfoReturnable<Optional<PaintingEntity>> cir) {
		//PaintingEntity paintingEntity = new PaintingEntity(world, pos);
		cir.setReturnValue(Optional.empty());
	}*/
}
