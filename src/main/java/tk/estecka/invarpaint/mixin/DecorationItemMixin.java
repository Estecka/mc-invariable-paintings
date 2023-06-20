package tk.estecka.invarpaint.mixin;

import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.estecka.invarpaint.InvariablePaintings;

import java.util.Optional;


@Mixin(DecorationItem.class)
public class DecorationItemMixin {
    private ItemStack itemStack;

        @Inject(
            method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/painting/PaintingEntity;placePainting(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Ljava/util/Optional;",
                    shift = At.Shift.BEFORE
            )
    )
    private void captureItemStack(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        itemStack = context.getStack();
    }

        @Redirect(
            method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/painting/PaintingEntity;placePainting(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Ljava/util/Optional;"
            )
    )
    private Optional<PaintingEntity> filterPlacedPainting(World world, BlockPos pos, Direction facing) {
            return InvariablePaintings.placePaintingWithItemStackContext(itemStack, world, pos, facing);
    }
}
