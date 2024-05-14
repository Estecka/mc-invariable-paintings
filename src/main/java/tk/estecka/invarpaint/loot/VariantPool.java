package tk.estecka.invarpaint.loot;

import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import static net.minecraft.registry.Registries.PAINTING_VARIANT;

public class VariantPool
{
	static public final TagKey<PaintingVariant> ALL_EXCLUSIVES = TagKey.of(RegistryKeys.PAINTING_VARIANT, new Identifier("invarpaint", "exclusive"));

	private Set<Identifier> pool = new HashSet<>();

	public VariantPool(){
	}

	public VariantPool(TagKey<PaintingVariant> tag){
		this.Add(tag);
	}

	public VariantPool Add(TagKey<PaintingVariant> tag){
		for (var e : PAINTING_VARIANT.iterateEntries(tag))
			pool.add(e.getKey().get().getValue());
		return this;
	}

	public VariantPool Remove(TagKey<PaintingVariant> tag){
		for (var e: PAINTING_VARIANT.iterateEntries(tag))
			pool.remove(e.getKey().get().getValue());
		return this;
	}

	public VariantPool RemoveFrom(TagKey<PaintingVariant> tag){
		Set<Identifier> result = new HashSet<>();
		for (var e : PAINTING_VARIANT.iterateEntries(tag)){
			Identifier id = e.getKey().get().getValue();
			if (!this.pool.contains(id))
				result.add(id);
		}
		this.pool = result;
		return this;
	}

	public VariantPool Invert(){
		Set<Identifier> inverse = new HashSet<>();
		for (Identifier id : PAINTING_VARIANT.getIds())
			if (!this.pool.contains(id))
				inverse.add(id);

		this.pool = inverse;
		return this;
	}

	public @Nullable Identifier GetRandom(Random random){
		Identifier[] array = this.pool.toArray(new Identifier[0]);
		if (array.length < 1)
			return null;

		int roll = random.nextInt(array.length);
		return array[roll];
	}

}
