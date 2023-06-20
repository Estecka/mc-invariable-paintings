package tk.estecka.invarpaint.mixin;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.DecorationItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
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

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static net.minecraft.entity.decoration.painting.PaintingEntity.readVariantFromNbt;

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
}
