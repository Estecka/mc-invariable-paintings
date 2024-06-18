package tk.estecka.invarpaint;

import java.util.Optional;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class InvarpaintClient
{
	static public Optional<Registry<PaintingVariant>> GetPaintingRegitry(){
		World world = MinecraftClient.getInstance().world;
		if (world != null)
			return world.getRegistryManager().getOptional(RegistryKeys.PAINTING_VARIANT);
		else
			return Optional.empty();
	}
}
