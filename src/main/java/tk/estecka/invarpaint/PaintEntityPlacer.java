package tk.estecka.invarpaint;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import org.joml.Vector2i;

import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.decoration.painting.PaintingVariants;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;

public class PaintEntityPlacer 
{
	/**
	 * Iterates over the possible positions a paintings could placed on, while still covering the block targeted by the player.
	 * Iteration is done in a ring-like pattern, so the positions closest to the targeted block are evaluated first.
	 * 
	 * Internally, all positions are relative to the top-right corner of the maximum scanned surface,
	 * but the values returned by the iteration are relative to the targeted block.
	 */
	static class	SurfaceIterator implements Iterator<Vector2i>
	{
		private final int width, height;
		/**
		 * The position of the targeted block within the scannable surface.
		 */
		private final Vector2i startPos;
		/**
		 * Keeps track of which blocks within the scannable surface have already been iterated on.
		 */
		private final boolean[][] checkList;
		private Queue<Vector2i> currentRing, nextRing;

		public SurfaceIterator (int variantWidth, int variantHeight){
			this.width = variantWidth;
			this.height = variantHeight;
			this.startPos = new Vector2i(variantWidth/2, variantHeight/2);
			this.checkList = new boolean[variantWidth][variantHeight];
			this.currentRing = new LinkedList<Vector2i>();
			this.nextRing    = new LinkedList<Vector2i>();


			boolean evenW = (variantWidth %2) == 0;
			boolean evenH = (variantHeight%2) == 0;
			
			currentRing.add(new Vector2i(startPos));
			if (evenW)
				currentRing.add(new Vector2i(startPos.x-1, startPos.y  ));
			if (evenH)
				currentRing.add(new Vector2i(startPos.x,   startPos.y-1));
			if (evenW && evenH)
				currentRing.add(new Vector2i(startPos.x-1, startPos.y-1));
			
			for (Vector2i p : currentRing)
				checkList[p.x][p.y] = true;
		}

		/**
		 * Returns positions adjacents to this one.
		 * Elements are ordered so as to start from the right, and circle counter-clockwise around the center.
		 */
		private Vector2i[]	GetAdjacents(Vector2i v){
			Vector2i[] r = new Vector2i[4];
			for (int i=0; i<r.length; ++i)
				r[i] = new Vector2i(v);

			int i=0;
			r[i++].add( 1,  0); // →
			r[i++].add( 0,  1); // ↑
			r[i++].add(-1,  0); // ←
			r[i++].add( 0, -1); // ↓
			// r[i++].add(-1, -1); // ↙
			// r[i++].add(-1,  1); // ↖
			// r[i++].add( 1,  1); // ↗
			// r[i++].add( 1, -1); // ↘

			return r;
		}

		private boolean ShouldEvaluate(Vector2i v){
			return (0 <= v.x)
			    && (0 <= v.y)
			    && (v.x < width)
			    && (v.y < height)
			    && !checkList[v.x][v.y]
			    ;
		}

		public boolean	hasNext(){
			return !currentRing.isEmpty() || !nextRing.isEmpty();
		}

		/**
		 * @return	The offset of the position to evaluate, relative to the targeted block.
		 */
		public Vector2i	next(){
			Vector2i pos;

			if (currentRing.isEmpty()){
				currentRing = nextRing;
				nextRing = new LinkedList<Vector2i>();
			}

			pos = currentRing.remove();
			for (Vector2i adj : GetAdjacents(pos))
			if (ShouldEvaluate(adj)){
				nextRing.add(adj);
				checkList[adj.x][adj.y] = true;
			}

			Vector2i offset = pos;
			offset.x -= this.startPos.x;
			offset.y -= this.startPos.y;
			return pos;
		}

	}

	static public Optional<PaintingEntity>	PlaceLockedPainting(World world, BlockPos targetPos, Direction facing, PaintingVariant variant){
		// PaintingEntity entity = new PaintingEntity(world, targetPos, facing, Registries.PAINTING_VARIANT.getEntry(variant));
		// if (entity.canStayAttached())
		// 	return Optional.of(entity);

		// The direction of the horizontal axis of the wall
		Vec3i right = facing.rotateYCounterclockwise().getVector();

		RegistryEntry<PaintingVariant> debugVariant = Registries.PAINTING_VARIANT.getEntry(PaintingVariants.ALBAN).get();

		int i=0;
		SurfaceIterator surface = new SurfaceIterator(variant.getWidth()/16, variant.getHeight()/16);
		while (surface.hasNext()) {
			Vector2i planeOffset = surface.next();
			Vec3i worldOffset = right.multiply(planeOffset.x);
			worldOffset = worldOffset.offset(Axis.Y, planeOffset.y);

			
			BlockPos pos = targetPos.add(worldOffset);
			if (!world.isClient) {
				InvariablePaintings.LOGGER.warn("[{}, {}]", planeOffset.x, planeOffset.y);
				PaintingEntity debugEntity = new PaintingEntity(world, pos, facing, debugVariant);
				debugEntity.setCustomName(Text.literal("---- " + String.valueOf(i++)));
				debugEntity.setCustomNameVisible(true);
				world.spawnEntity(debugEntity);
			}
		}

		return Optional.empty();
	}
}
