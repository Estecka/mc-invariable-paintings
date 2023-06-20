package tk.estecka.invarpaint.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PaintingEntity.class)
public interface PaintingEntityAccessor {
    @Accessor("VARIANT")
    private static TrackedData<RegistryEntry<PaintingVariant>> getVariant() {
        throw new AssertionError();
    }
}
