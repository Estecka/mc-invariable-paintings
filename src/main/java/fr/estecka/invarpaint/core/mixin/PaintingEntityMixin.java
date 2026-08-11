package fr.estecka.invarpaint.core.mixin;

import fr.estecka.invarpaint.api.PaintStackUtil;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

@Mixin(Painting.class)
public class PaintingEntityMixin
{
	@ModifyExpressionValue( method="dropItem", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/decoration/painting/Painting;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;") )
	private ItemEntity setDropVariant(ItemEntity original) {
		ItemStack stack = original.getItem();

		if (stack.is(Items.PAINTING))
			PaintStackUtil.SetVariant(stack, (Painting)(Object)this);

		return original;
	}
}
