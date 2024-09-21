package tk.estecka.invarpaint.pick.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import tk.estecka.invarpaint.core.PaintStackUtil;

@Mixin(ItemGroups.class)
public class ItemGroupsMixin
{
	@ModifyArg( method="method_48935", remap=false, index=0, at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemGroup$Entries;add(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemGroup$StackVisibility;)V") )
	static private ItemStack SetPaintingComponents(ItemStack stack){
		String variantName  = PaintStackUtil.GetVariantName(stack);
		if (variantName != null)
			PaintStackUtil.SetModel(stack, variantName);
		return stack;
	}
}
