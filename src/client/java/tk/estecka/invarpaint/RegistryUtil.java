package tk.estecka.invarpaint;

import java.util.Optional;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class RegistryUtil
{
	static public Optional<Registry<PaintingVariant>> GetPaintingRegitry(){
		Registry<PaintingVariant> result = null;

		World world = MinecraftClient.getInstance().world;
		if (world != null)
			result = world.getRegistryManager().get(RegistryKeys.PAINTING_VARIANT);

		return Optional.ofNullable(result);
	}
}
