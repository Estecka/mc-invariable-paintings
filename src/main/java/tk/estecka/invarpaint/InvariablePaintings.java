package tk.estecka.invarpaint;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.PaintingVariantTags;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.minecraft.entity.decoration.painting.PaintingEntity.readVariantFromNbt;

public class InvariablePaintings implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Invar-Paint");

	@Override
	public void onInitialize() {
		SellPaintingFactory.RegisterPaintings();
		LockVariantRandomlyLootFunction.RegisterFunction();
	}

	public static Optional<PaintingEntity> placePaintingWithItemStackContext(ItemStack itemStack, World world, BlockPos pos, Direction facing) {
		RegistryEntry<PaintingVariant> registryEntry;
		if (itemStack.getItem() instanceof DecorationItem && itemStack.hasNbt()) {
			NbtCompound nbtCompound = itemStack.getNbt().getCompound("EntityTag");
			registryEntry = readVariantFromNbt(nbtCompound).orElseThrow();
		} else {
			registryEntry = null;
		}
		PaintingEntity paintingEntity = new PaintingEntity(world, pos);
		List<RegistryEntry<PaintingVariant>> list = new ArrayList<>();
		Iterable<RegistryEntry<PaintingVariant>> iterateEntries = Registries.PAINTING_VARIANT.iterateEntries(PaintingVariantTags.PLACEABLE);
		Objects.requireNonNull(list);
		iterateEntries.forEach(list::add);
		if (list.isEmpty()) {
			return Optional.empty();
		} else {
			paintingEntity.setFacing(facing);
			list.removeIf((variant) -> {
				paintingEntity.setVariant(variant);
				return registryEntry != null && variant == registryEntry && !paintingEntity.canStayAttached();
			});
			if (list.isEmpty()) {
				return Optional.empty();
			} else {
				int i = list.stream().mapToInt(InvariablePaintings::getSize).max().orElse(0);
				list.removeIf((variant) -> getSize(variant) < i);
				Optional<RegistryEntry<PaintingVariant>> optional = Util.getRandomOrEmpty(list, Random.create());
				if (optional.isEmpty()) {
					return Optional.empty();
				} else {
					paintingEntity.setVariant(optional.get());
					paintingEntity.setFacing(facing);
					return Optional.of(paintingEntity);
				}
			}
		}
	}

	private static int getSize(RegistryEntry<PaintingVariant> variant) {
		return variant.value().getWidth() * variant.value().getHeight();
	}
}
