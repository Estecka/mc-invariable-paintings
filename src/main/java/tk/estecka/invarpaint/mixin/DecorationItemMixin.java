package tk.estecka.invarpaint.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.PaintingVariantTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.minecraft.entity.decoration.painting.PaintingEntity.readVariantFromNbt;

@Mixin(DecorationItem.class)
public class DecorationItemMixin {

    @Mutable @Final @Shadow private final EntityType<? extends AbstractDecorationEntity> entityType;

    private ItemStack itemStack;

    public DecorationItemMixin(EntityType<? extends AbstractDecorationEntity> entityType) {
        this.entityType = entityType;
    }

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
        /*
        ItemStack itemStack = context.getStack();
        if (itemStack.getItem() instanceof DecorationItem && itemStack.hasNbt()) {
            NbtCompound nbtCompound = context.getStack().getNbt().getCompound("EntityTag");
            String variant = context.getStack().getNbt().getCompound("EntityTag").getString("variant");
            //if (!(context.getWorld() instanceof ClientWorld)) {
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockPos blockPos2 = blockPos.offset(direction);
            System.out.println(direction);
            PaintingEntity paintingEntity = new PaintingEntity(context.getWorld(), blockPos2);


            //System.out.println(paintingEntity.canStayAttached());
            //RegistryEntry<PaintingVariant> registryEntry = (RegistryEntry)readVariantFromNbt(nbtCompound);
            RegistryEntry<PaintingVariant> registryEntry = readVariantFromNbt(nbtCompound).orElseThrow();
            paintingEntity.setVariant(registryEntry);
            System.out.println(registryEntry);
            System.out.println(paintingEntity);
            System.out.println(paintingEntity.canStayAttached());
            if (paintingEntity.canStayAttached()) {

            }
                //PaintingEntity paintingEntity = (PaintingEntity) this.entityType.create(world);
            //}
        }*/
    }


        @Redirect(
            method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/painting/PaintingEntity;placePainting(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Ljava/util/Optional;"
            )
    )
    private Optional<PaintingEntity> filterPlacedPainting(World world, BlockPos pos, Direction facing) {
            RegistryEntry<PaintingVariant> registryEntry;
            if (itemStack.getItem() instanceof DecorationItem && itemStack.hasNbt()) {
                NbtCompound nbtCompound = itemStack.getNbt().getCompound("EntityTag");
                //String variant = itemStack.getNbt().getCompound("EntityTag").getString("variant");
                //System.out.println(facing);
                PaintingEntity paintingEntity = new PaintingEntity(world, pos.offset(facing));


                //RegistryEntry<PaintingVariant> registryEntry = readVariantFromNbt(nbtCompound).orElseThrow();
                registryEntry = readVariantFromNbt(nbtCompound).orElseThrow();

                System.out.println("||||"+registryEntry);
            } else {
                registryEntry = null;
            }
            PaintingEntity paintingEntity = new PaintingEntity(world, pos);
            List<RegistryEntry<PaintingVariant>> list = new ArrayList();
            Iterable<RegistryEntry<PaintingVariant>> var10000 = Registries.PAINTING_VARIANT.iterateEntries(PaintingVariantTags.PLACEABLE);
            Objects.requireNonNull(list);
            var10000.forEach(list::add);
            if (list.isEmpty()) {
                return Optional.empty();
            } else {
                paintingEntity.setFacing(facing);
                list.removeIf((variant) -> {
                    paintingEntity.setVariant(variant);
                    System.out.println(variant+", "+paintingEntity+", "+paintingEntity.canStayAttached());
                    return variant == registryEntry && !paintingEntity.canStayAttached();
                });
                if (list.isEmpty()) {
                    return Optional.empty();
                } else {
                    int i = list.stream().mapToInt(DecorationItemMixin::getSize).max().orElse(0);
                    list.removeIf((variant) -> getSize(variant) < i);
                    Optional<RegistryEntry<PaintingVariant>> optional = Util.getRandomOrEmpty(list, Random.create());
                    if (optional.isEmpty()) {
                        return Optional.empty();
                    } else {
                        paintingEntity.setVariant(optional.get());
                        paintingEntity.setFacing(facing);
                        return Optional.of(paintingEntity);
                    }
                }
            }


        //System.out.println("test: "+itemStack.getNbt());
            /*
            if (itemStack.getItem() instanceof DecorationItem && itemStack.hasNbt()) {
                NbtCompound nbtCompound = itemStack.getNbt().getCompound("EntityTag");
                //String variant = itemStack.getNbt().getCompound("EntityTag").getString("variant");
                //System.out.println(facing);
                PaintingEntity paintingEntity = new PaintingEntity(world, pos.offset(facing));


                RegistryEntry<PaintingVariant> registryEntry = readVariantFromNbt(nbtCompound).orElseThrow();
                paintingEntity.setVariant(registryEntry);
                paintingEntity.setFacing(facing);
                System.out.println(registryEntry);
                System.out.println(paintingEntity);
                System.out.println(paintingEntity.canStayAttached());
                if (paintingEntity.canStayAttached()) {
                    return Optional.of(paintingEntity);
                }
            }
            return Optional.empty();
            */



        //if (this.entityType == EntityType.PAINTING) {
//
        //}
    }

    private static int getSize(RegistryEntry<PaintingVariant> variant) {
        return ((PaintingVariant)variant.value()).getWidth() * ((PaintingVariant)variant.value()).getHeight();
    }
}
