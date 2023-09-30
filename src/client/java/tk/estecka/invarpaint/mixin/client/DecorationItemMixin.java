package tk.estecka.invarpaint.mixin.client;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.estecka.invarpaint.PaintStackUtil;
import tk.estecka.invarpaint.TooltipUtil;
import java.util.List;

@Mixin(DecorationItem.class)
public abstract class DecorationItemMixin 
{

	@Inject( method="appendTooltip", at=@At("TAIL") )
	public void condenseTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
		if (stack.isOf(Items.PAINTING)) {
			String variantId = PaintStackUtil.GetVariantId(stack);

			if (variantId != null || !context.isCreative())
				TooltipUtil.RemoveOriginalTooltip(tooltip);

			if (variantId != null)
				TooltipUtil.AddVariantTooltip(tooltip, variantId, context.isAdvanced());
		}
	}

}
