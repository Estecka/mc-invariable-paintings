package fr.estecka.invarpaint.tooltip.mixin;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;


@Mixin(HangingEntityItem.class)
public abstract class DecorationItemMixin
{

	@Redirect(
		method="appendHoverText",
		at = @At(value="INVOKE", target="net/minecraft/world/entity/decoration/painting/PaintingVariant.title()Ljava/util/Optional;")
	)
	private Optional<Component> RemoveTitle(PaintingVariant variant){
		return Optional.empty();
	}

	@Redirect(
		method="appendHoverText",
		at = @At(value="INVOKE", target="net/minecraft/world/entity/decoration/painting/PaintingVariant.author()Ljava/util/Optional;")
	)
	private Optional<Component> RemoveAuthor(PaintingVariant variant){
		return Optional.empty();
	}


	@ModifyExpressionValue(
		method = "appendHoverText",
		at = @At(value="INVOKE", target="net/minecraft/network/chat/Component.translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;")
	)
	private MutableComponent AddCondensedTooltip(MutableComponent original, @Local Holder<PaintingVariant> variant, @Local(argsOnly=true) Consumer<Component> tooltipAdder, @Local(argsOnly=true) TooltipFlag tooltipType) {
		Optional<Component> author = variant.value().author();
		if (author.isPresent())
			original = original.append(Component.literal(" ")).append(author.get());
		return original;
	}

	@Inject(
		method = "appendHoverText",
		at = @At(value="INVOKE", ordinal=0, shift=Shift.AFTER, target="java/util/function/Consumer.accept(Ljava/lang/Object;)V")
	)
	private void AddRawVariant(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type, CallbackInfo ci, @Local Holder<PaintingVariant> variant) {
		if (type.equals(TooltipFlag.ADVANCED))
			textConsumer.accept(Component.literal(variant.getRegisteredName()).withStyle(ChatFormatting.DARK_GRAY));
	}

}
