package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.Compostable;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.WorkAtComposter;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorkAtComposter.class)
public abstract class WorkAtComposterMixin {
    @Shadow
    @Final
    private static List<Item> COMPOSTABLE_ITEMS;

    public abstract void spawnComposterFillEffects(ServerLevel level, BlockState preState, BlockPos pos, BlockState postState);

    @Inject(at = @At("HEAD"), method = "compostItems", cancellable = true)
    private void compostItems(ServerLevel serverLevel, Villager villager, GlobalPos globalPos, BlockState preBlockState, CallbackInfo ci) {
        BlockPos blockpos = globalPos.pos();
        if (preBlockState.getValue(ComposterBlock.LEVEL) == 8) {
            preBlockState = ComposterBlock.extractProduce(villager, preBlockState, serverLevel, blockpos);
        }

        int i = 20;
        Reference2IntMap<Item> amounts = new Reference2IntOpenHashMap<>(COMPOSTABLE_ITEMS.size() * 2);
        SimpleContainer simpleContainer = villager.getInventory();
        BlockState postBlockState = preBlockState;

        for (ItemStack itemStack : simpleContainer) {
            Compostable compostable = (Compostable) itemStack.getItemHolder().getData(SimpleLibraryDataMaps.COMPOSTABLES);
            if (compostable != null && compostable.canVillagerCompost()) {
                int itemStackCount = itemStack.getCount();
                int k1 = amounts.getInt(itemStack.getItem()) + itemStackCount;
                amounts.put(itemStack.getItem(), k1);
                int l1 = Math.min(Math.min(k1 - 10, i), itemStackCount);
                if (l1 > 0) {
                    i -= l1;

                    for (int i2 = 0; i2 < l1; i2++) {
                        postBlockState = ComposterBlock.insertItem(villager, postBlockState, serverLevel, itemStack, blockpos);
                        if (postBlockState.getValue(ComposterBlock.LEVEL) == 7) {
                            this.spawnComposterFillEffects(serverLevel, preBlockState, blockpos, postBlockState);
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }
}
