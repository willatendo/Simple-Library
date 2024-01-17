package willatendo.simplelibrary.mixin.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import willatendo.simplelibrary.server.entity.SimpleShearable;

@Mixin(Sheep.class)
public class SheepMixin implements SimpleShearable {
	private Sheep sheep = ((Sheep) (Object) this);

	@Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
	private void removeVanillaFunction(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> callbackInfoReturnable) {
		if (player.getItemInHand(interactionHand).is(Items.SHEARS)) {
			callbackInfoReturnable.setReturnValue(InteractionResult.PASS);
		}
	}

	@Override
	public boolean isShearable(ItemStack itemStack, Level level, BlockPos blockPos) {
		return this.sheep.readyForShearing();
	}

	@Override
	public List<ItemStack> onSheared(Player player, ItemStack itemStack, Level level, BlockPos blockPos, int fortune) {
		level.playSound(null, blockPos, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
		this.sheep.gameEvent(GameEvent.SHEAR, player);
		if (!level.isClientSide()) {
			this.sheep.setSheared(true);
			int items = 1 + this.sheep.getRandom().nextInt(3);
			List<ItemStack> itemStacks = new ArrayList<>();
			for (int i = 0; i < items; i++) {
				itemStacks.add(new ItemStack(Sheep.ITEM_BY_DYE.get(this.sheep.getColor())));
			}
			return itemStacks;
		}
		return Collections.emptyList();
	}
}
