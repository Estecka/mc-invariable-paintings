package tk.estecka.invarpaint.crafting;

import net.minecraft.entity.decoration.painting.PaintingVariant;
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

	static public int	CombinationToIndex(long code, int setSize){
		int result = 0;

		for (int i=0; i<setSize; ++i) {
			result += n_choose_k((int)(code>>(4*i)) & 0xf, i+1);
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
