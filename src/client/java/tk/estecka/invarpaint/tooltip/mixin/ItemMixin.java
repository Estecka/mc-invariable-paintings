package tk.estecka.invarpaint.tooltip.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import tk.estecka.invarpaint.tooltip.TooltipUtil;

@Mixin(Item.class)
public abstract class ItemMixin 
{

	@ModifyReturnValue( method="getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at=@At("RETURN") )
	private Text	GetNameWithVariant(Text name, ItemStack stack){
		if (stack.isOf(Items.PAINTING))
			name = TooltipUtil.AppendPaintingName(name.copy(), stack);

		return name;
	}

}
