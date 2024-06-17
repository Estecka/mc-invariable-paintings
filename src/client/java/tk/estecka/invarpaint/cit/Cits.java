package tk.estecka.invarpaint.cit;

import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public class Cits
{
	static public final ModelIdentifier CIT_MISSING = ModelIdentifier.ofInventoryVariant(Identifier.of("invarpaint", "missing_painting"));
	static public final ModelIdentifier CIT_FILLED  = ModelIdentifier.ofInventoryVariant(Identifier.of("invarpaint", "filled_painting" ));
	static public final ModelIdentifier CIT_RANDOM  = ModelIdentifier.ofInventoryVariant(Identifier.of("invarpaint", "random_painting" ));

	static public final String CIT_PREFIX = "item/painting/";
}
