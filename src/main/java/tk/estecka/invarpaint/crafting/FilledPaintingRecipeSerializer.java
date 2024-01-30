package tk.estecka.invarpaint.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.dynamic.Range;

public class FilledPaintingRecipeSerializer 
implements RecipeSerializer<FilledPaintingRecipe>
{

	@Override
	public FilledPaintingRecipe read(Identifier id, JsonObject json){
		JsonObject ingredients = JsonHelper.getObject(json, "ingredients");
		JsonObject dyeCount = JsonHelper.getObject(ingredients, "dyeCount");
		Range<Integer> range =  Range.CODEC.decode(new Dynamic<JsonElement>(JsonOps.INSTANCE, dyeCount)).getOrThrow(false, JsonParseException::new).getFirst();

		return new FilledPaintingRecipe(
			id,
			CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(json, "category", null), CraftingRecipeCategory.MISC),
			range,
			JsonHelper.getBoolean(ingredients, "acceptsBlank" , true ),
			JsonHelper.getBoolean(ingredients, "acceptsFilled", false),
			JsonHelper.getBoolean(json, "obfuscated", false)
		);
	}

	@Override
	public FilledPaintingRecipe read(Identifier id, PacketByteBuf packet){
		Range<Integer> range = new Range<Integer>(
			packet.getInt(0),
			packet.getInt(1)
		);
		return new FilledPaintingRecipe(
			id,
			packet.readEnumConstant(CraftingRecipeCategory.class),
			range,
			packet.getBoolean(0),
			packet.getBoolean(1),
			packet.getBoolean(2)
		);
	}

	@Override
	public void	write(PacketByteBuf packet, FilledPaintingRecipe recipe){
		packet.writeEnumConstant(recipe.getCategory());
		packet.setInt(0, recipe.dyesMin);
		packet.setInt(1, recipe.dyesMax);
		packet.setBoolean(0, recipe.canCreate);
		packet.setBoolean(1, recipe.canDerive);
		packet.setBoolean(2, recipe.isObfuscated);
	}
}
