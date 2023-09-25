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
}
