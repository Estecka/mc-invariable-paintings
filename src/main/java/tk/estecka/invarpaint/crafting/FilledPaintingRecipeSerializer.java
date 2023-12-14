package tk.estecka.invarpaint.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.dynamic.Range;

public class FilledPaintingRecipeSerializer 
implements RecipeSerializer<FilledPaintingRecipe>
{
	public record Ingredients(boolean blank, boolean filled, Range<Integer> dyes) {
		static public final Codec<Ingredients> CODEC = RecordCodecBuilder.create(builder -> 
			builder.group(
				Codec.BOOL.fieldOf("acceptsBlank" ).orElse(true ).<Ingredients>forGetter(Ingredients::blank),
				Codec.BOOL.fieldOf("acceptsFilled").orElse(false).<Ingredients>forGetter(Ingredients::filled),
				Range.CODEC.fieldOf("dyeCount").<Ingredients>forGetter(Ingredients::dyes)
			).apply(
				builder,
				Ingredients::new
			)
		);

		static public Ingredients ofRecipe(FilledPaintingRecipe r){
			return new Ingredients(r.canCreate, r.canDerive, new Range<Integer>(r.dyesMin, r.dyesMax));
		}
	}

	static private final Codec<FilledPaintingRecipe> CODEC = RecordCodecBuilder.create(builder ->
		builder.group(
			CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(FilledPaintingRecipe::getCategory),
			Codec.BOOL.fieldOf("obfuscated").orElse(false).forGetter(FilledPaintingRecipe::IsObfuscated),
			Ingredients.CODEC.fieldOf("ingredients").<FilledPaintingRecipe>forGetter(Ingredients::ofRecipe)
		).apply(
			builder, 
			(cat, obf, ing) -> new FilledPaintingRecipe(cat, ing.dyes(), ing.blank(), ing.filled(), obf)
		)
	);

	

	@Override
	public Codec<FilledPaintingRecipe> codec(){
		return CODEC;
	}

	@Override
	public FilledPaintingRecipe read(PacketByteBuf packet){
		Range<Integer> range = new Range<Integer>(
			packet.getInt(0),
			packet.getInt(1)
		);
		return new FilledPaintingRecipe(
			packet.readEnumConstant(CraftingRecipeCategory.class),
			range,
			packet.getBoolean(0),
			packet.getBoolean(1),
			packet.getBoolean(2)
		);
	}

	@Override
	public void	write(PacketByteBuf packet, FilledPaintingRecipe recipe){
		packet.writeString(recipe.getCategory().asString());
		packet.setInt(0, recipe.dyesMin);
		packet.setInt(1, recipe.dyesMax);
		packet.setBoolean(0, recipe.canCreate);
		packet.setBoolean(1, recipe.canDerive);
		packet.setBoolean(2, recipe.isObfuscated);
	}
}
