package tk.estecka.invarpaint.loot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import static tk.estecka.invarpaint.InvarpaintMod.MODID;

public class InvarpaintLoot
implements ModInitializer
{
	@Override
	public void onInitialize() {
		SellPaintingFactory.Register();
		LockVariantRandomlyLootFunction.Register();
		RegisterPack("looting", "Painting Loot", true);
	}

	private void RegisterPack(String id, String displayName, boolean defaultEnabled){
		final var mod = FabricLoader.getInstance().getModContainer(MODID).get();
		ResourceManagerHelper.registerBuiltinResourcePack(
			Identifier.of(MODID, id), mod,
			Text.literal(displayName),
			defaultEnabled ? ResourcePackActivationType.DEFAULT_ENABLED : ResourcePackActivationType.NORMAL
		);
	}

}
