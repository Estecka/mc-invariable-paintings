package fr.estecka.invarpaint.loot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import static fr.estecka.invarpaint.InvarpaintMod.MODID;

public class InvarpaintLoot
implements ModInitializer
{
	@Override
	public void onInitialize() {
		LockVariantRandomlyLootFunction.Register();
		RegisterPack("looting", "Painting Loot", true);
	}

	private void RegisterPack(String id, String displayName, boolean defaultEnabled){
		final var mod = FabricLoader.getInstance().getModContainer(MODID).get();
		ResourceLoader.registerBuiltinPack(
			Identifier.fromNamespaceAndPath(MODID, id), mod,
			defaultEnabled ? PackActivationType.DEFAULT_ENABLED : PackActivationType.NORMAL
		);
	}

}
