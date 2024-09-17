package fr.estecka.invarpaint.core.mixin;

import java.util.Optional;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.estecka.invarpaint.InvarpaintMod;
import fr.estecka.invarpaint.core.PaintEntityPlacer;
import fr.estecka.invarpaint.api.PaintStackUtil;


@Mixin(DecorationItem.class)
public abstract class DecorationItemMixin 
{
	@WrapOperation( method="useOnBlock", at=@At(value="INVOKE", target="Lnet/minecraft/entity/decoration/painting/PaintingEntity;placePainting(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Ljava/util/Optional;") )
	private Optional<PaintingEntity> filterPlacedPainting(World world, BlockPos pos, Direction facing, Operation<Optional<PaintingEntity>> original, ItemUsageContext context) {
		final var registry = world.getRegistryManager().getOrThrow(RegistryKeys.PAINTING_VARIANT);
		Optional<PaintingEntity> result;
		String variantName = PaintStackUtil.GetVariantName(context.getStack());
		Identifier variantId = (variantName==null) ? null : Identifier.tryParse(variantName);
		RegistryEntry<PaintingVariant> itemVariant = registry.getEntry(variantId).orElse(null);
		PlayerEntity player = context.getPlayer();

		if (itemVariant != null) {
			result = PaintEntityPlacer.PlaceLockedPainting(world, pos, facing, itemVariant);
			if (result.isEmpty() && player != null) {
				player.sendMessage(
					InvarpaintMod.ServersideTranslatable("painting.invalid_space",
						PaintStackUtil.TranslatableVariantName(variantId).formatted(Formatting.YELLOW),
						Text.translatable("painting.dimensions", itemVariant.value().width(), itemVariant.value().height())
					),
					true
				);
			}
		}
		else if (player.isCreative() || (variantName!=null && InvarpaintMod.IsNokebabInstalled()))
			result = original.call(world, pos, facing);
		else
			result = Optional.empty();

		// Fix for vanilla clients consuming unplaced paintings
		if (result.isEmpty() && !world.isClient() && !player.isCreative())
			player.currentScreenHandler.updateToClient();

		return result;
	}

}
