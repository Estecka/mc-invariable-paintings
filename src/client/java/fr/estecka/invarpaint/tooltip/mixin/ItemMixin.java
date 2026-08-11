package fr.estecka.invarpaint.tooltip.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fr.estecka.invarpaint.tooltip.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Mixin(Item.class)
public abstract class ItemMixin
{

	@ModifyReturnValue( method="getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;", at=@At("RETURN") )
	private Component GetNameWithVariant(Component name, ItemStack stack){
		if (stack.is(Items.PAINTING))
			name = TooltipUtil.AppendPaintingName(name.copy(), stack);

		return name;
	}

}
