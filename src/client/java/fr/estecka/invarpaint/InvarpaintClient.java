package fr.estecka.invarpaint;

import java.util.Optional;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.level.Level;

public class InvarpaintClient
implements ClientModInitializer
{
	public void	onInitializeClient(){
		var mod = FabricLoader.getInstance().getModContainer(InvarpaintMod.MODID).get();
		ResourceLoader.registerBuiltinPack(
			Identifier.fromNamespaceAndPath(InvarpaintMod.MODID, "vanilla-cit"),
			mod,
			PackActivationType.DEFAULT_ENABLED
		);
	}

	@Deprecated
	static public Optional<Registry<PaintingVariant>> GetPaintingRegitry(){
		Level world = Minecraft.getInstance().level;
		if (world != null)
			return world.registryAccess().lookup(Registries.PAINTING_VARIANT);
		else
			return Optional.empty();
	}
}
