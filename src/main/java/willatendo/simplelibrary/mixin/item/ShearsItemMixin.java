package willatendo.simplelibrary.mixin.item;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import willatendo.simplelibrary.server.entity.SimpleShearable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin extends Item {
	public ShearsItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
		if (livingEntity instanceof SimpleShearable simpleShearable) {
			Level level = livingEntity.level();
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			BlockPos blockPos = BlockPos.containing(livingEntity.position());
			if (simpleShearable.isShearable(itemStack, level, blockPos)) {
				List<ItemStack> itemStacks = simpleShearable.onSheared(player, itemStack, level, blockPos, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, itemStack));
				RandomSource randomSource = RandomSource.create();
				itemStacks.forEach(itemStackIn -> {
					ItemEntity itemEntity = livingEntity.spawnAtLocation(itemStackIn, 1.0F);
					itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((double) (randomSource.nextFloat() - randomSource.nextFloat()) * 0.1F, (double) (randomSource.nextFloat() - randomSource.nextFloat()) * 0.05F, (double) (randomSource.nextFloat() - randomSource.nextFloat()) * 0.1F));
				});
				itemStack.hurtAndBreak(1, player, holder -> holder.broadcastBreakEvent(interactionHand));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
