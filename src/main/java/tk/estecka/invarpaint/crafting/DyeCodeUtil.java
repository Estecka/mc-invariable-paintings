package tk.estecka.invarpaint.crafting;

import net.minecraft.registry.Registries;

/**
 * DyeMask: Unordered set of dyes stored in 16 bits (short). Every bit indicates
 * the absence or presence of the with the id corresponding to the bit index.
 * 
 * DyeCode: Ordered list of dyes  where every 4-bits (0x0 to 0xf) indicates  the
 * id of a dye. int32 can store 8 dyes, int64 (long) can store 16.
 */
public class DyeCodeUtil
{
	static public final int	COMBINATION_MAX = n_choose_k(16, 8);

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
	 * Converts any combination index to a variant's index.
	 */
	static public int	Comb2Var(int combId){
		return combId * Registries.PAINTING_VARIANT.size() / COMBINATION_MAX;
	}
	/**
	 * Converts a variant's index to the first corresponding combination index.
	 */
	static public int	Var2Comb(int rawId){
		return rawId * COMBINATION_MAX / Registries.PAINTING_VARIANT.size();
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
