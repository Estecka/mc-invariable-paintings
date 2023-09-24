package tk.estecka.invarpaint.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.estecka.invarpaint.InvariablePaintings;
import tk.estecka.invarpaint.PaintEntityPlacer;
import tk.estecka.invarpaint.PaintStackUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(DecorationItem.class)
public class DecorationItemMixin extends Item {

	@Mutable
	@Final
	@Shadow
	private final EntityType<? extends AbstractDecorationEntity> entityType;

	private ItemStack itemStack;
	private PlayerEntity player;

	public DecorationItemMixin(Settings settings, EntityType<? extends AbstractDecorationEntity> entityType) {
		super(settings);
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
	private void captureContext(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		itemStack = context.getStack();
		player = context.getPlayer();
	}

	@Redirect(
		method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
		at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/entity/decoration/painting/PaintingEntity;placePainting(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Ljava/util/Optional;"
		)
	)
	private Optional<PaintingEntity> filterPlacedPainting(World world, BlockPos pos, Direction facing) {
		String variantId = PaintStackUtil.GetVariantId(this.itemStack);
		PaintingVariant itemVariant = (variantId==null) ? null : Registries.PAINTING_VARIANT.get(new Identifier(variantId));

		if (itemVariant != null) {
			Optional<PaintingEntity> entity = PaintEntityPlacer.PlaceLockedPainting(world, pos, facing, itemVariant);
			if (entity.isEmpty() && player != null) {
				player.sendMessage(
					Text.translatable("painting.invalid_space",
					Text.translatable("painting."+variantId.replace(":",".")+".title").formatted(Formatting.YELLOW),
					Text.translatable("painting.dimensions", MathHelper.ceilDiv(itemVariant.getWidth(), 16), MathHelper.ceilDiv(itemVariant.getHeight(), 16)).formatted(Formatting.WHITE)),
					true
				);
			}
			return entity;
		}
		else {
			if (variantId != null)
				InvariablePaintings.LOGGER.warn("Unknown painting id: {}", variantId);
			return PaintingEntity.placePainting(world, pos, facing);
		}
	}

	@Inject(
		method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V",
		at = @At(
				value = "TAIL"
		)
	)
	public void condenseTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
		if (this.entityType == EntityType.PAINTING) {
			AtomicReference<Text> atmAuthor = new AtomicReference<Text>(null);
			AtomicReference<Text> atmSize   = new AtomicReference<Text>(null);
			tooltip.removeIf(text -> {
				TextContent textContent = text.getContent();
				if (textContent instanceof TranslatableTextContent) {
					String key = ((TranslatableTextContent) textContent).getKey();
					if (key.equals("painting.random"))
						return true;
					else if (key.startsWith("painting.") && key.endsWith(".title"))
						return true;
					else if (key.startsWith("painting.") && key.endsWith(".author")){
						atmAuthor.set(text);
						return true;
					}
					else if (key.equals("painting.dimensions")){
						atmSize.set(text);
						return true;
					}
				}
				return false;
			});

			if (PaintStackUtil.GetVariantId(stack) == null)
				tooltip.add(Text.translatable("painting.random").formatted(Formatting.GRAY));
			else {
				Text author = atmAuthor.get();
				Text size   = atmSize.get();
				MutableText authorLine = Text.empty();
				if (size!=null)
					authorLine.append(size);
				if (size!=null && author!=null)
					authorLine.append(" ");
				if (author!=null)
					authorLine.append(author);
				tooltip.add(authorLine);
			}
		}
	}

	@Override
	public Text getName(ItemStack stack) {
		if (this.entityType == EntityType.PAINTING) {
			String variantId = PaintStackUtil.GetVariantId(stack);
			if (variantId != null) {
				return Text.translatable(this.getTranslationKey(stack)) // I could just use translatable variables,
				           .append(Text.literal(" (")                   // but this way is compatible with other languages
				           .append(Text.translatable("painting."+variantId.replace(":",".")+".title")
				           .append(")")).formatted(Formatting.YELLOW))
				           ;
			}
		}
		return super.getName();
	}
}
