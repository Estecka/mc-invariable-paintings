package tk.estecka.invarpaint.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class FilledPaintingRecipeSerializer 
implements RecipeSerializer<FilledPaintingRecipe>
{

	@Override
	public FilledPaintingRecipe read(Identifier id, JsonObject json){
		return new FilledPaintingRecipe(
			id,
			CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(json, "category", null), CraftingRecipeCategory.MISC),
			JsonHelper.getBoolean(json, "acceptsBlank",  true ),
			JsonHelper.getBoolean(json, "acceptsFilled", false),
			JsonHelper.getBoolean(json, "obfuscated",    false)
		);
	}

	@Override
	public FilledPaintingRecipe read(Identifier id, PacketByteBuf packet){
		return new FilledPaintingRecipe(
			id,
			CraftingRecipeCategory.CODEC.byId(packet.readString(), CraftingRecipeCategory.MISC),
			packet.getBoolean(0),
			packet.getBoolean(1),
			packet.getBoolean(2)
		);
	}

	@Override
	public void	write(PacketByteBuf packet, FilledPaintingRecipe recipe){
		packet.writeString(recipe.getCategory().asString());
		packet.setBoolean(0, recipe.canCreate);
		packet.setBoolean(1, recipe.canDerive);
		packet.setBoolean(2, recipe.isObfuscated);
	}
}
