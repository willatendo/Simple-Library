package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.RaidHeroGift;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GiveGiftToHero.class)
public class GiveGiftToHeroMixin {
    @Inject(at = @At("HEAD"), method = "getLootTableToThrow", cancellable = true)
    private static void getLootTableToThrow(Villager villager, CallbackInfoReturnable<ResourceKey<LootTable>> cir) {
        if (!villager.isBaby()) {
            RaidHeroGift raidHeroGift = (RaidHeroGift) villager.getVillagerData().profession().getData(SimpleLibraryDataMaps.RAID_HERO_GIFTS);
            if (raidHeroGift != null) {
                cir.setReturnValue(raidHeroGift.lootTable());
            }
        }
    }
}
