package tk.estecka.invarpaint.mixin;

import java.util.Collection;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.recipe.Recipe;
import tk.estecka.invarpaint.crafting.IUnsyncRecipe;

@Mixin(SynchronizeRecipesS2CPacket.class)
public abstract class SynchronizeRecipesS2CPacketMixin {
	@Shadow @Final private List<Recipe<?>> recipes;

	/**
	 * Prevents custom recipes from being synched with the client, allowing the 
	 * mod to remain optional there.
	 */
	@Inject(method="<init>(Ljava/util/Collection;)V", at=@At("TAIL"))
	void	SkipServerOnly(Collection<?> recipes, CallbackInfo info){
		this.recipes.removeIf(r -> r instanceof IUnsyncRecipe unsync && unsync.DontSync());
	}
}
