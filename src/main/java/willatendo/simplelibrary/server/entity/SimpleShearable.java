package willatendo.simplelibrary.server.entity;

import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface SimpleShearable {
	default boolean isShearable(ItemStack itemStack, Level level, BlockPos blockPos) {
		return true;
	}

	default List<ItemStack> onSheared(Player player, ItemStack itemStack, Level level, BlockPos blockPos, int fortune) {
		return Collections.emptyList();
	}
}
