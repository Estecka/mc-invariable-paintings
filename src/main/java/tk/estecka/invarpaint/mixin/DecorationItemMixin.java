package tk.estecka.invarpaint.mixin;

import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.estecka.invarpaint.InvariablePaintings;
import tk.estecka.invarpaint.PaintStackCreator;

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
        String variantId = PaintStackCreator.GetVariantId(this.itemStack);
        PaintingVariant itemVariant = (variantId==null) ? null : Registries.PAINTING_VARIANT.get(new Identifier(variantId));

        if (itemVariant != null) {
            PaintingEntity entity = new PaintingEntity(world, pos, facing, Registries.PAINTING_VARIANT.getEntry(itemVariant));
            if (entity.canStayAttached())
                return Optional.of(entity);
            else
                return Optional.empty();
        }
        else {
            if (variantId != null)
                InvariablePaintings.LOGGER.warn("Unknown painting id: {}", variantId);
            return PaintingEntity.placePainting(world, pos, facing);
        }
    }
}
