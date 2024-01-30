package tk.estecka.invarpaint.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.CraftingScreenHandler;
import tk.estecka.invarpaint.PaintStackUtil;
import tk.estecka.invarpaint.crafting.IObfuscatedRecipe;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin 
{
	@WrapOperation( method="updateResult", at=@At( value="INVOKE", target="net/minecraft/recipe/CraftingRecipe.craft (Lnet/minecraft/inventory/Inventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;") )
	static private ItemStack	ShouldObfuscate(CraftingRecipe recipe, Inventory input, DynamicRegistryManager manager, Operation<ItemStack> original, @Share("obf") LocalBooleanRef doObfuscate){
		if (recipe instanceof IObfuscatedRecipe obfRecipe && obfRecipe.IsObfuscated())
			doObfuscate.set(true);
		return original.call(recipe, input, manager);
	}


	@ModifyArg( method="updateResult", index=3, at=@At(value="INVOKE", target="net/minecraft/network/packet/s2c/play/ScreenHandlerSlotUpdateS2CPacket.<init> (IIILnet/minecraft/item/ItemStack;)V") )
	static private ItemStack ObfuscateResult(int syncId, int revision, int slot, ItemStack stack, @Share("obf") LocalBooleanRef doObfuscate){
		if (doObfuscate.get())
			stack = PaintStackUtil.Obfuscate(stack);
		return stack;
	}
}
