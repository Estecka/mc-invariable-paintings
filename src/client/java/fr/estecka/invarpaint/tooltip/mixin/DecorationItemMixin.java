package fr.estecka.invarpaint.tooltip.mixin;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fr.estecka.invarpaint.api.PaintStackUtil;
import fr.estecka.invarpaint.tooltip.TooltipUtil;

@Mixin(DecorationItem.class)
public abstract class DecorationItemMixin 
{

	// @Inject( method="appendTooltip", at=@At("HEAD") )
	// public void condenseTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent display, Consumer<Text> tooltipAdder, TooltipType type, CallbackInfo ci) {
	// 	if (stack.isOf(Items.PAINTING) && paint) {
	// 		Identifier variantId = PaintStackUtil.GetVariantId(stack);

	// 		if (variantId != null || !type.isCreative())
	// 			TooltipUtil.RemoveOriginalTooltip(tooltip);

	// 		if (variantId != null)
	// 			TooltipUtil.AddVariantTooltip(tooltip, variantId, type.isAdvanced());
	// 	}
	// }

}
