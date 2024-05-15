package tk.estecka.invarpaint.loot;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import static net.minecraft.registry.Registries.PAINTING_VARIANT;

public record PoolIdentifier(boolean isNegative, boolean isTag, Identifier id)
{
	static public final  Codec<PoolIdentifier> CODEC = Codec.STRING.comapFlatMap(PoolIdentifier::Parse, p->p.toString());

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

	public boolean Exists(){
		if (id == null)
			return false;
		if (this.isTag)
			return PAINTING_VARIANT.streamTags().anyMatch(tag -> tag.id().equals(this.id));
		else
			return PAINTING_VARIANT.containsId(this.id);
	}

	public Set<Identifier> GetPool(){
		Set<Identifier> pool = new HashSet<>();

		if (!this.isTag)
			pool.add(this.id);
		else for (var e : PAINTING_VARIANT.iterateEntries(TagKey.of(RegistryKeys.PAINTING_VARIANT, this.id)))
			pool.add(e.getKey().get().getValue());

		if (this.isNegative){
			Set<Identifier> inverse = new HashSet<>();
			for (Identifier id : PAINTING_VARIANT.getIds())
			if  (!pool.contains(id))
					inverse.add(id);
			pool = inverse;
		}

		return pool;
	}

	static public @Nullable Identifier GetRandom(Collection<PoolIdentifier> list, Random random){
		Set<Identifier> pool = new HashSet<>();
		for (PoolIdentifier poolId : list)
			pool.addAll(poolId.GetPool());

		Identifier[] array = pool.toArray(new Identifier[0]);
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
