package tk.estecka.invarpaint.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import tk.estecka.invarpaint.PaintStackUtil;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@WrapOperation( method="doItemPick", at=@At(value="INVOKE", target="net/minecraft/entity/Entity.getPickBlockStack ()Lnet/minecraft/item/ItemStack;") )
	private ItemStack	PickPaintingVariant(Entity entity, Operation<ItemStack> original){
		ItemStack item = original.call(entity);
		if (entity instanceof PaintingEntity painting)
			PaintStackUtil.SetVariant(item, painting.writeNbt(new NbtCompound()).getString("variant"));

		return item;
	}
}
