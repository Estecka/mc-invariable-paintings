package tk.estecka.invarpaint.crafting;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * DyeMask: Unordered set of dyes stored in 16 bits (short). Every bit indicates
 * the absence or presence of the with the id corresponding to the bit index.
 * 
 * DyeCode: Ordered list of dyes  where every 4-bits (0x0 to 0xf) indicates  the
 * id of a dye. int32 can store 8 dyes, int64 (long) can store 16.
 */
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

	static public long	MaskToCode(short mask){
		long code = 0;
		byte n = 0;
		for (byte b=0; b<16; ++b){
			if ((mask & (1<<b)) != 0) {
				code |= (long)b << n;
				n += 4;
			}
		}
		return code;
	}

	/**
	 * https://en.wikipedia.org/wiki/Combinatorial_number_system
	 * @param code A dyeCode were dyes are ordered from smallest to largest.
	 */
	static public int	CombinationToIndex(long code, int setSize){
		int result = 0;

		for (int i=0; i<setSize; ++i) {
			result += n_choose_k((int)(code>>(4*i)) & 0xf, i+1);
		}

		return result;
	}
	/**
	 * https://en.wikipedia.org/wiki/Combinatorial_number_system#Finding_the_k-combination_for_a_given_number
	 * @param rawId
	 * @return A dyeMask
	 */
	static public short	IndexToCombination(int rawId, int setSize){
		short result = 0x0;

		while (0 < setSize) {
			int pos = 0;
			int dye = 0;

			for (int n=setSize-1; true; ++n){
				int nCk = n_choose_k(n, setSize);
				if (rawId < nCk)
					break;
				pos = nCk;
				dye = n;
			}

			result |= 1 << dye;
			rawId -= pos;
			setSize--;
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
}
