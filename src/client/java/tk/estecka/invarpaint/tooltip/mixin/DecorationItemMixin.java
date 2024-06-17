package tk.estecka.invarpaint.tooltip.mixin;

import java.util.List;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.estecka.invarpaint.core.PaintStackUtil;
import tk.estecka.invarpaint.tooltip.TooltipUtil;

@Mixin(DecorationItem.class)
public abstract class DecorationItemMixin 
{

	@Inject( method="appendTooltip", at=@At("TAIL") )
	public void condenseTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
		if (stack.isOf(Items.PAINTING)) {
			String variantId = PaintStackUtil.GetVariantId(stack);

			if (variantId != null || !type.isCreative())
				TooltipUtil.RemoveOriginalTooltip(tooltip);

			if (variantId != null)
				TooltipUtil.AddVariantTooltip(tooltip, variantId, type.isAdvanced());
		}
	}

}
