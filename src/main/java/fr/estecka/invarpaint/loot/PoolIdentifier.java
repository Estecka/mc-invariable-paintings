package fr.estecka.invarpaint.loot;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public record PoolIdentifier(boolean isNegative, boolean isTag, Identifier id)
{
	static public final  Codec<PoolIdentifier> CODEC = Codec.STRING.comapFlatMap(PoolIdentifier::Parse, PoolIdentifier::toString);

	static public DataResult<PoolIdentifier> Parse(String data){
		boolean neg = false;
		if (data.length() < 1)
			return DataResult.error(()-> "Empty identifier");
		if(data.startsWith("!")){
			data = data.substring(1);
			neg = true;
		}

		boolean tag = false;
		if (data.length() < 1)
			return DataResult.error(()-> "Empty identifier");
		if(data.startsWith("#")){
			data = data.substring(1);
			tag = true;
		}

		Identifier id = Identifier.tryParse(data);

		return DataResult.success(new PoolIdentifier(neg, tag, id));
	}

	public boolean Exists(Registry<PaintingVariant> registry){
		if (id == null)
			return false;
		if (this.isTag)
			return registry.getTags().anyMatch(tag -> tag.key().location().equals(this.id));
		else
			return registry.containsKey(this.id);
	}

	public Set<Holder<PaintingVariant>> GetPool(Registry<PaintingVariant> registry){
		Set<Holder<PaintingVariant>> pool = new HashSet<>();

		if (!this.isTag){
			var optEntry = registry.get(this.id);
			if (optEntry.isEmpty())
				pool.add(optEntry.get());
		}
		else for (var entry : registry.getTagOrEmpty(TagKey.create(Registries.PAINTING_VARIANT, this.id)))
			pool.add(entry);

		if (this.isNegative){
			Set<Holder<PaintingVariant>> inverse = new HashSet<>();
			var it = registry.listElements().iterator();
			while (it.hasNext()){
				var entry = it.next();
				if (!pool.contains(entry))
					inverse.add(entry);
			}
			pool = inverse;
		}

		return pool;
	}

	static public @Nullable Holder<PaintingVariant> GetRandom(Collection<PoolIdentifier> list, RandomSource random, Registry<PaintingVariant> registry){
		Set<Holder<PaintingVariant>> pool = new HashSet<>();
		for (PoolIdentifier poolId : list)
			pool.addAll(poolId.GetPool(registry));

		@SuppressWarnings("unchecked")
		Holder<PaintingVariant>[] array = pool.toArray(Holder[]::new);
		if (array.length < 1)
			return null;

		int roll = random.nextInt(array.length);
		return array[roll];
	}

	@Override
	public String toString(){
		String r = this.id.toString();
		if (this.isTag)
			r = "#"+r;
		if (this.isNegative)
			r = "!"+r;
		return r;
	}
}
