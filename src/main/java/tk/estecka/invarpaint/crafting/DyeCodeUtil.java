package tk.estecka.invarpaint.crafting;

import java.util.SortedSet;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.DyeItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

public class DyeCodeUtil
{
	/**
	 * Removes bits from the code until it matches a variant in the registry.
	 */
	static RegistryEntry<PaintingVariant>	FindFromDyecode(int dyeCode){
		for (int mask=0xffffffff; mask!=0; mask>>>=1){
			dyeCode &= mask;
			var entry = Registries.PAINTING_VARIANT.getEntry(dyeCode);
			if (entry.isPresent())
				return entry.get();
		}

		return Registries.PAINTING_VARIANT.getIndexedEntries().getOrThrow(0);
	}

	static int	UnorderedDyeset2RawId(SortedSet<DyeItem> dyeSet){
		int result = 0;

		int i = 1;
		for (DyeItem dye : dyeSet) {
			int dyeId = dye.getColor().getId();
			result += n_choose_k(dyeId, i);
			++i;
		}

		return result;
	}

	static public int n_choose_k(int n, int k){
		int result = 1;

		for (int i=0; i<k; ++i)
			result *= n-i;

		for (int i=2; i<=k; ++i)
			result /= i;

		return result;
	}

	static public int SuccessiveSum(int min, int max){
		return (min + max) * (1 + max - min) / 2;
	}

	static public int SuccessiveSuccesiveSumSum(int min, int max){
		int result = 0;
		int succesive = 0;
		for (int i=min; i<=max; ++i){
			succesive += i;
			result += succesive;
		}

		return result;
	}
}
