package tk.estecka.invarpaint.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import tk.estecka.invarpaint.TooltipUtil;

@Mixin(Item.class)
public abstract class ItemMixin 
{

	@Inject( method="getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at=@At("RETURN") )
	void	GetNameWithVariant(ItemStack stack, CallbackInfoReturnable<Text> info){
		if (stack.isOf(Items.PAINTING) && (info.getReturnValue() instanceof MutableText))
			TooltipUtil.AppendPaintingName((MutableText)info.getReturnValue(), stack);
	}

}
