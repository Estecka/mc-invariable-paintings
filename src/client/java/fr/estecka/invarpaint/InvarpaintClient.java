package fr.estecka.invarpaint;

import java.util.Optional;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class InvarpaintClient
implements ClientModInitializer
{
	public void	onInitializeClient(){
		var mod = FabricLoader.getInstance().getModContainer(InvarpaintMod.MODID).get();
		ResourceManagerHelper.registerBuiltinResourcePack(
			Identifier.of(InvarpaintMod.MODID, "vanilla-cit"),
			mod,
			Text.literal("Invariable-Paintings CITs"),
			ResourcePackActivationType.DEFAULT_ENABLED
		);
	}

	static public Optional<Registry<PaintingVariant>> GetPaintingRegitry(){
		@SuppressWarnings("resource")
		World world = MinecraftClient.getInstance().world;
		if (world != null)
			return world.getRegistryManager().getOptional(RegistryKeys.PAINTING_VARIANT);
		else
			return Optional.empty();
	}
}
