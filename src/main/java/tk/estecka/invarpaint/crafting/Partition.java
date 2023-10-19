package tk.estecka.invarpaint.crafting;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Represents a fraction of  the existing  painting variants, containing as many
 * as the number of dye combinations.
 * 
 * Partitions are only relevants when the number of combinations is smaller than
 * the number of existing paintings; for example, if the number of dyes required
 * for crafting  is capped to 1. The input variant is then  used as a parameter,
 * affecting which partition the crafting result will be pulled from.
 */
public class Partition 
{
	private int index;
	private int size;

	public Partition(int size){
		this.index = 0;
		this.size = size;
	}

	/**
	 * @param inputVariant 
	 * The variant  of the painting  placed into  the crafting,  or null  if the
	 * painting is blank. Will fallback to null if the id is invalid or does not
	 * exist.
	 * 
	 * @implNote
	 * (index & 1) makes it so  one partition out of two  will have its crafting
	 * direction flipped. This way, trying to iterate onto a painting  using the
	 * same dye over and over will always yield the same two paintings.
	 * 
	 * (input <= output) makes it so  only a portion  of the partition  has  its
	 * direction flipped. So it is always possible to progress in both direction
	 * by alternating with the correct dyes.
	 */
	static public Partition FromIngredients(@Nullable String inputVariant, int combinationMax, int combinationRank){
		Partition r = new Partition(combinationMax);

		Identifier id;
		if ((null!=inputVariant) && (null!=(id=Identifier.tryParse(inputVariant))) && Registries.PAINTING_VARIANT.containsId(id)) {
			int rawId = Registries.PAINTING_VARIANT.getRawId(Registries.PAINTING_VARIANT.get(id));
			int inputRank = rawId % r.size;
			r.index = rawId / r.size;

			boolean forward = ((r.index & 1) != 0) ^ (inputRank <= combinationRank);
			if (forward) 
				r.Next();
			else
				r.Previous();

		}

		return r;
	}

	/**
	 * @param rank	The index of the variant within the partition. I.e, the rank
	 * of the dye combination used for crafting.
	 */
	public Optional<? extends RegistryEntry<PaintingVariant>>	GetVariant(int rank){
		int index = (this.index * this.size) + rank;

		if (index >= Registries.PAINTING_VARIANT.size())
			index = rank;

		return Registries.PAINTING_VARIANT.getEntry(index);
	}

	public Partition Next(){
		++this.index;
		if (Registries.PAINTING_VARIANT.size() <= (this.index * this.size))
			this.index = 0;
		return this;
	}

	public Partition Previous(){
		--this.index;
		if (this.index < 0)
			this.index = Registries.PAINTING_VARIANT.size() / this.size;
		return this;
	}

}
