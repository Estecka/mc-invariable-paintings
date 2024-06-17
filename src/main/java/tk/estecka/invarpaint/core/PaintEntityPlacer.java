package tk.estecka.invarpaint.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import org.joml.Vector2i;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class PaintEntityPlacer 
{
	/**
	 * Iterates over  the possible positions  where a painting  could be  placed
	 * and still cover up  the block  targeted by the player.  Iteration is done
	 * in a ring-like pattern,  such that the positions  closest to the targeted
	 * block are evaluated first.
	 * 
	 * Internally, all positions  are relative to  the bottom-left corner of the
	 * scanned surface,  but the values returned by the iterator are relative to
	 * the targeted block.
	 */
	static class SurfaceIterator
	implements Iterator<Vector2i>
	{
		private final int width, height;
		/**
		 * The position of the targeted block within the scannable surface.
		 */
		private final Vector2i targetBlock;
		/**
		 * Keeps track of which positions have already been added to the queue.
		 */
		private final boolean[][] checkList;
		private final Queue<Vector2i> queue;

		public SurfaceIterator (int variantWidth, int variantHeight){
			this.width = variantWidth;
			this.height = variantHeight;
			this.targetBlock = new Vector2i(variantWidth/2, variantHeight/2);
			this.checkList = new boolean[variantWidth][variantHeight];
			this.queue = new LinkedList<Vector2i>();


			boolean evenW = (variantWidth %2) == 0;
			boolean evenH = (variantHeight%2) == 0;

			queue.add(new Vector2i(targetBlock));
			if (evenW)
				queue.add(new Vector2i(targetBlock).add(-1,  0));
			if (evenH)
				queue.add(new Vector2i(targetBlock).add( 0, -1));
			if (evenW && evenH)
				queue.add(new Vector2i(targetBlock).add(-1, -1));
			
			for (Vector2i p : queue)
				checkList[p.x][p.y] = true;
		}

		/**
		 * Returns positions adjacent to this one.
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
			return !queue.isEmpty();
		}

		/**
		 * @return	The offset of the position to evaluate, relative to the targeted block.
		 */
		public Vector2i	next(){
			Vector2i pos = queue.remove();

			for (Vector2i adj : GetAdjacents(pos))
			if (ShouldEvaluate(adj)){
				queue.add(adj);
				checkList[adj.x][adj.y] = true;
			}

			Vector2i offset = pos;
			offset.x -= this.targetBlock.x;
			offset.y -= this.targetBlock.y;
			return offset;
		}

	}

	static public Optional<PaintingEntity>	PlaceLockedPainting(World world, BlockPos targetPos, Direction facing, RegistryEntry<PaintingVariant> variant){
		PaintingEntity entity = new PaintingEntity(world, targetPos, facing, variant);
		if (entity.canStayAttached())
			return Optional.of(entity);

		// The direction of the horizontal axis of the wall
		Vec3i right = facing.rotateYCounterclockwise().getVector();

		SurfaceIterator surface = new SurfaceIterator(variant.value().width(), variant.value().height());
		surface.next(); // Skip the targeted position, which was already tested.
		while (surface.hasNext()) {
			Vector2i planeOffset = surface.next();
			Vec3i worldOffset = right.multiply(planeOffset.x).add(0, planeOffset.y, 0);

			entity.setPosition(
				targetPos.getX() + worldOffset.getX(),
				targetPos.getY() + worldOffset.getY(),
				targetPos.getZ() + worldOffset.getZ()
			);
			if (entity.canStayAttached())
				return Optional.of(entity);
		}

		return Optional.empty();
	}
}
