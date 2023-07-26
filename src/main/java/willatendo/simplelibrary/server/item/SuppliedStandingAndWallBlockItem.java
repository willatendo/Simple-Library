package willatendo.simplelibrary.server.item;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

/* 
 * A {@link StandingAndWallBlockItem} that uses Suppliers instead of a Blocks
 * 
 * @author Willatendo
 */
public class SuppliedStandingAndWallBlockItem extends SuppliedBlockItem {
	protected final Supplier<Block> wallBlock;
	private final Direction attachmentDirection;

	public SuppliedStandingAndWallBlockItem(Supplier<Block> standingBlock, Supplier<Block> wallBlock, Properties properties, Direction direction) {
		super(standingBlock, properties);
		this.wallBlock = wallBlock;
		this.attachmentDirection = direction;
	}

	protected boolean canPlace(LevelReader levelReader, BlockState blockState, BlockPos blockPos) {
		return blockState.canSurvive(levelReader, blockPos);
	}

	@Override
	protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext) {
		BlockState blockState = this.wallBlock.get().getStateForPlacement(blockPlaceContext);
		BlockState placedState = null;
		LevelReader levelReader = blockPlaceContext.getLevel();
		BlockPos blockPos = blockPlaceContext.getClickedPos();

		for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
			if (direction != this.attachmentDirection.getOpposite()) {
				BlockState toPlace = direction == this.attachmentDirection ? this.getBlock().getStateForPlacement(blockPlaceContext) : blockState;
				if (toPlace != null && this.canPlace(levelReader, toPlace, blockPos)) {
					placedState = toPlace;
					break;
				}
			}
		}

		return placedState != null && levelReader.isUnobstructed(placedState, blockPos, CollisionContext.empty()) ? placedState : null;
	}

	@Override
	public void registerBlocks(Map<Block, Item> blockToItemMap, Item item) {
		super.registerBlocks(blockToItemMap, item);
		blockToItemMap.put(this.wallBlock.get(), item);
	}

	@Override
	public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item item) {
		super.removeFromBlockToItemMap(blockToItemMap, item);
		blockToItemMap.remove(this.wallBlock.get());
	}
}
