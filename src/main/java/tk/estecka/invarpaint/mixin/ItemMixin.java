package tk.estecka.invarpaint.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tk.estecka.invarpaint.PaintStackUtil;

@Mixin(Item.class)
public abstract class ItemMixin 
{
	@Inject( method="getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at=@At("RETURN") )
	void	GetNameWithVariant(ItemStack stack, CallbackInfoReturnable<Text> info){
		String variantId;

		if ((stack.isOf(Items.PAINTING))
		 && (info.getReturnValue() instanceof MutableText)
		 && (null != (variantId=PaintStackUtil.GetVariantId(stack)))
		) {
			// I could just use translatable variables,
			// but this way is compatible with other languages
			MutableText text = (MutableText)info.getReturnValue();
			text.append(
				Text.literal(" (")
				    .append(Text.translatableWithFallback("painting."+variantId.replace(":",".")+".title", variantId))
				    .append(")")
				    .formatted(Formatting.YELLOW)
			);
		}
	}

}
