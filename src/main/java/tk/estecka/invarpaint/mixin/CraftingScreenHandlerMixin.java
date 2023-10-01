package tk.estecka.invarpaint.mixin;

import java.util.Optional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.world.World;
import tk.estecka.invarpaint.InvariablePaintings;
import tk.estecka.invarpaint.PaintStackUtil;
import tk.estecka.invarpaint.crafting.FilledPaintingRecipe;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin 
{
	static private boolean doObfuscate = false;

	@WrapOperation( method="updateResult", at=@At( value="INVOKE", target="net/minecraft/recipe/RecipeManager.getFirstMatch (Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/Optional;") )
	static private Optional<RecipeEntry<CraftingRecipe>>	GetObfuscationMode(RecipeManager instance, RecipeType<?> type, Inventory ingredients, World world, Operation<Optional<RecipeEntry<CraftingRecipe>>> operation){
		var recipe = operation.call(instance, type, ingredients, world);
		doObfuscate = recipe.isPresent()
		           && recipe.get().value() instanceof FilledPaintingRecipe 
		           && world.getGameRules().getBoolean(InvariablePaintings.OBFUSCATE_RULE)
		           ;
		return recipe;
	}


	@ModifyArg( method="updateResult", index=3, at=@At(value="INVOKE", target="net/minecraft/network/packet/s2c/play/ScreenHandlerSlotUpdateS2CPacket.<init> (IIILnet/minecraft/item/ItemStack;)V") )
	static private ItemStack ObfuscateResult(int syncId, int revision, int slot, ItemStack stack){
		if (doObfuscate)
			stack = PaintStackUtil.Obfuscate(stack);
		return stack;
	}
}
