package fr.estecka.invarpaint.tooltip.mixin;

import java.util.Optional;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.DecorationItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
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
	private MutableText AddCondensedTooltip(MutableText original, @Local RegistryEntry<PaintingVariant> variant) {
		Optional<Text> author = variant.value().author();
		if (author.isPresent())
			original = original.append(Text.literal(" ")).append(author.get());
		return original;
	}

}
