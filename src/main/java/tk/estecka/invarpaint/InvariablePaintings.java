package tk.estecka.invarpaint;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvariablePaintings implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Invar-Paint");

	@Override
	public void onInitialize() {
		SellPaintingFactory.RegisterPaintings();
		LockVariantRandomlyLootFunction.RegisterFunction();
	}
}
