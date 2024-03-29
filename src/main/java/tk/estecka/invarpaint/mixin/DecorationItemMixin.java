package tk.estecka.invarpaint.mixin;

import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import tk.estecka.invarpaint.InvariablePaintings;
import tk.estecka.invarpaint.PaintEntityPlacer;
import tk.estecka.invarpaint.PaintStackUtil;
import java.util.Optional;

@Mixin(DecorationItem.class)
public abstract class DecorationItemMixin 
{
	private ItemStack itemStack;
	private PlayerEntity player;

	@Inject( method="useOnBlock", at=@At("HEAD") )
	private void captureContext(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		itemStack = context.getStack();
		player = context.getPlayer();
	}

	@WrapOperation( method="useOnBlock", at=@At(value="INVOKE", target="Lnet/minecraft/entity/decoration/painting/PaintingEntity;placePainting(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Ljava/util/Optional;") )
	private Optional<PaintingEntity> filterPlacedPainting(World world, BlockPos pos, Direction facing, Operation<Optional<PaintingEntity>> original) {
		String variantId = PaintStackUtil.GetVariantId(this.itemStack);
		Identifier id = (variantId==null) ? null : Identifier.tryParse(variantId);
		Optional<PaintingVariant> itemVariant = Registries.PAINTING_VARIANT.getOrEmpty(id);

		if (itemVariant.isPresent()) {
			Optional<PaintingEntity> entity = PaintEntityPlacer.PlaceLockedPainting(world, pos, facing, itemVariant.get());
			if (entity.isEmpty() && player != null) {
				player.sendMessage(
					Text.translatable("painting.invalid_space",
						Text.translatableWithFallback(id.toTranslationKey("painting", "title"), variantId).formatted(Formatting.YELLOW),
						Text.translatable("painting.dimensions", itemVariant.get().getWidth()/16, itemVariant.get().getHeight()/16).formatted(Formatting.WHITE)
					),
					true
				);
			}
			return entity;
		}
		else if (player.isCreative() || (variantId!=null && InvariablePaintings.IsNokebabInstalled()))
			return original.call(world, pos, facing);
		else
			return Optional.empty();
	}

}
