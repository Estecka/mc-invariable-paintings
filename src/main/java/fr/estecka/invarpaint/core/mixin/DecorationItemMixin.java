package fr.estecka.invarpaint.core.mixin;

import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.estecka.invarpaint.core.PaintEntityPlacer;
import fr.estecka.invarpaint.api.PaintStackUtil;
import fr.estecka.invarpaint.api.PaintTextUtil;


@Mixin(HangingEntityItem.class)
public abstract class DecorationItemMixin 
{
	@WrapOperation( method="useOn", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/decoration/painting/Painting;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Ljava/util/Optional;") )
	private Optional<Painting> filterPlacedPainting(Level world, BlockPos pos, Direction facing, Operation<Optional<Painting>> original, UseOnContext context) {
		Optional<Painting> result;
		Holder<PaintingVariant> variantEntry = PaintStackUtil.GetVariantEntry(context.getItemInHand());
		Player player = context.getPlayer();

		if (variantEntry != null) {
			result = PaintEntityPlacer.PlaceLockedPainting(world, pos, facing, variantEntry);
			if (result.isEmpty() && player != null) {
				player.sendOverlayMessage(
					PaintTextUtil.ServersideTranslatable("painting.invalid_space",
						PaintTextUtil.TranslatableVariantName(variantEntry.getRegisteredName()).withStyle(ChatFormatting.YELLOW),
						Component.translatable("painting.dimensions", variantEntry.value().width(), variantEntry.value().height())
					)
				);
			}
		}
		else if (player.isCreative() /*|| (variantName!=null && InvarpaintMod.IsNokebabInstalled())*/)
			result = original.call(world, pos, facing);
		else
			result = Optional.empty();

		// Fix for vanilla clients consuming unplaced paintings
		// https://bugs.mojang.com/browse/MC-257133
		if (result.isEmpty() && !world.isClientSide() && !player.isCreative())
			player.containerMenu.broadcastFullState();

		return result;
	}

}
