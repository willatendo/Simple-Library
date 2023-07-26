package willatendo.simplelibrary.server.block;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

/*
 * A class filled with Block helpers
 * 
 * @author Willatendo
 */
public class BlockHelper {
	/*
	 * Used for BlockEntities
	 * 
	 * @param blockEntityAtPos The {@link BlockEntityType} at the position
	 * 
	 * @param blockEntityType The {@link BlockEntityType}
	 * 
	 * @param blockEntityTicker The method that will tick
	 * 
	 * @return a {@link BlockEntityTicker}
	 */
	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityTypeAtPos, BlockEntityType<E> blockEntityType, BlockEntityTicker<? super E> blockEntityTicker) {
		return blockEntityType == blockEntityTypeAtPos ? (BlockEntityTicker<A>) blockEntityTicker : null;
	}
}
