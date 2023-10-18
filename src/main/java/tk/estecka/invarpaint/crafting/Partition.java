package tk.estecka.invarpaint.crafting;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.InvariablePaintings;

/**
 * Represents  a  group  of all variants  that can be  crafted  using a specific
 * number of dyes.
 * Partitions are only relevants when the number of possible dye combinations is
 * considerably smaller than the number of paintings; for example, if the number
 * of dyes required  for crafting is capped to 1. The resulting variant  will be
 * pulled from a different partition, depending on the input painting's variant,
 * allowing  all  variants  beyond  the  dyes' range  to  become  craftable,  by
 * reapplying to already crafted paintings.
 */
public class Partition 
{
	private int offset;
	private int size;

	public Partition(int size){
		this.offset = 0;
		this.size = size;
	}

	public Partition(@Nullable PaintingVariant variant, int size){
		this(size);
		this.FocusVariant(variant);
	}

	public Partition(@Nullable String variantId, int size){
		this(size);
		this.FocusVariant(variantId);
	}

	/**
	 * @param rank	The rank of the dye combination used for crafting.
	 */
	public Optional<? extends RegistryEntry<PaintingVariant>>	GetVariant(int rank){
		int index = this.offset + rank;

		if (index >= Registries.PAINTING_VARIANT.size())
			index = rank;

		return Registries.PAINTING_VARIANT.getEntry(index);
	}


	public Partition Next(){
		this.offset += this.size;
		if (this.offset >= Registries.PAINTING_VARIANT.size())
			this.offset = 0;
		return this;
	}

	public Partition FocusVariant(@Nullable String variantId){
		if (variantId != null)
			this.FocusVariant(Registries.PAINTING_VARIANT.get(Identifier.tryParse(variantId)));
		else
			this.offset = 0;
		return this;
	}

	public Partition FocusVariant(@Nullable PaintingVariant variant){
		if (variant != null){
			this.offset = Registries.PAINTING_VARIANT.getRawId(variant);
			this.offset -= (offset % size);
		}
		else
			this.offset = 0;
		return this;
	}

}
