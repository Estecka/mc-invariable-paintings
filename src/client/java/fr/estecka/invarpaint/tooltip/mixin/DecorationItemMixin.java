package fr.estecka.invarpaint.tooltip.mixin;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;


@Mixin(DecorationItem.class)
public abstract class DecorationItemMixin 
{

	@Redirect(
		method="appendTooltip",
		at = @At(value="INVOKE", target="net/minecraft/entity/decoration/painting/PaintingVariant.title()Ljava/util/Optional;")
	)
	private Optional<Text> RemoveTitle(PaintingVariant variant){
		return Optional.empty();
	}

	@Redirect(
		method="appendTooltip",
		at = @At(value="INVOKE", target="net/minecraft/entity/decoration/painting/PaintingVariant.author()Ljava/util/Optional;")
	)
	private Optional<Text> RemoveAuthor(PaintingVariant variant){
		return Optional.empty();
	}


	@ModifyExpressionValue(
		method = "appendTooltip",
		at = @At(value="INVOKE", target="net/minecraft/text/Text.translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;")
	)
	private MutableText AddCondensedTooltip(MutableText original, @Local RegistryEntry<PaintingVariant> variant, @Local(argsOnly=true) Consumer<Text> tooltipAdder, @Local(argsOnly=true) TooltipType tooltipType) {
		Optional<Text> author = variant.value().author();
		if (author.isPresent())
			original = original.append(Text.literal(" ")).append(author.get());
		return original;
	}

	@Inject(
		method = "appendTooltip",
		at = @At(value="INVOKE", ordinal=0, shift=Shift.AFTER, target="java/util/function/Consumer.accept(Ljava/lang/Object;)V")
	)
	private void AddRawVariant(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type, CallbackInfo ci, @Local RegistryEntry<PaintingVariant> variant) {
		if (type.equals(TooltipType.ADVANCED))
			textConsumer.accept(Text.literal(variant.getIdAsString()).formatted(Formatting.DARK_GRAY));
	}

}
