package fr.estecka.invarpaint.pick.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import fr.estecka.invarpaint.InvarpaintMod;
import fr.estecka.invarpaint.api.PaintStackUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin
{
	@ModifyArg(
		method = "lambda$generatePresetPaintings$0",
		index = 0,
		at=@At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/CreativeModeTab$Output;accept(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/CreativeModeTab$TabVisibility;)V"
		)
	)
	static private ItemStack SetPaintingComponents(ItemStack stack){
		if (InvarpaintMod.CONFIG.setItemModel){
			Identifier variantId = PaintStackUtil.GetVariantId(stack);
			if (variantId != null)
				PaintStackUtil.SetModel(stack, variantId);
		}
		return stack;
	}
}
