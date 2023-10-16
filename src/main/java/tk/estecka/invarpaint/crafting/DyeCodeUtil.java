package tk.estecka.invarpaint.crafting;

import java.util.Optional;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * DyeMask: Unordered set of dyes stored in 16 bits (short). Every bit indicates
 * the absence or presence of the dye with the id corresponding to its index.
 * 
 * DyeCode: Ordered list of dyes  where every 4-bits (0x0 to 0xf) indicates  the
 * id of a dye. int32 can store 8 dyes, int64 (long) can store 16.
 */
public class DyeCodeUtil
{
	static public final int[]	COMBINATION_MAX;

	static {
		COMBINATION_MAX = new int[9];
		for (int i=0; i<COMBINATION_MAX.length; ++i)
			COMBINATION_MAX[i] = n_choose_k(16, i);
	}

/******************************************************************************/
/* Fast Lane                                                                  */
/******************************************************************************/

	static public Optional<? extends RegistryEntry<PaintingVariant>>	DyemaskToVariant(short mask){
		return Registries.PAINTING_VARIANT.getEntry(RankToVariant(MaskToRank(mask), MaskSize(mask)));
	}

	static public short	VariantToDyemask(PaintingVariant variant, int setSize){
		return RankToMask(VariantToRank(Registries.PAINTING_VARIANT.getRawId(variant), setSize), setSize);
	}

/******************************************************************************/
/* Details                                                                    */
/******************************************************************************/

	static public int MaskSize(short mask){
		int setSize = 0;
		for (int i=0; i<16; ++i, mask>>>=1)
			if ((mask & 1) != 0)
				++setSize;
		return setSize;
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

	static public short	CodeToMask(long code, int setSize){
		short mask = 0;
		for (int i=0; i<setSize; ++i, code>>>=4)
			mask |= (short)1 << (code & 0xf);

		return mask;
	}

	/**
	 * Converts any combination rank to a variant's index.
	 */
	static public int	RankToVariant(int rank, int setSize){
		return rank * Registries.PAINTING_VARIANT.size() / COMBINATION_MAX[setSize];
	}
	/**
	 * Converts a variant's index to the rank of the first corresponding combination.
	 */
	static public int	VariantToRank(int rawId, int setSize){
		return CeilDivide(rawId * COMBINATION_MAX[setSize], Registries.PAINTING_VARIANT.size());
	}

	static int CeilDivide(int numerator, int denomitator){
		return (numerator+denomitator-1) / denomitator;
	}

	/**
	 * https://en.wikipedia.org/wiki/Combinatorial_number_system
	 * https://stackoverflow.com/a/3948303
	 */
	static public int	MaskToRank(short mask){
		int rank = 0;

		for (int n=0,k=1; n<16; ++n,mask>>>=1){
			if ((mask & 1) != 0)
				rank += n_choose_k(n, k++);
		}

		return rank;
	}
	/**
	 * https://en.wikipedia.org/wiki/Combinatorial_number_system#Finding_the_k-combination_for_a_given_number
	 */
	static public short	RankToMask(int rank, int setSize){
		short result = 0x0;

		while (0 < setSize) {
			int pos = 0;
			int dye = 0;

			for (int n=setSize-1; true; ++n){
				int nCk = n_choose_k(n, setSize);
				if (rank < nCk)
					break;
				pos = nCk;
				dye = n;
			}

			result |= 1 << dye;
			rank -= pos;
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
