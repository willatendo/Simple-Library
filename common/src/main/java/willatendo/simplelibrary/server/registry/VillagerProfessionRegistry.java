package willatendo.simplelibrary.server.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;

public final class VillagerProfessionRegistry extends SimpleRegistry<VillagerProfession> {
    VillagerProfessionRegistry(String modId) {
        super(Registries.VILLAGER_PROFESSION, modId);
    }

    public SimpleHolder<VillagerProfession> registerSimple(String id, ResourceKey<PoiType> jobSite, SoundEvent workSound) {
        return this.register(id, () -> new VillagerProfession(id, heldJobSite -> heldJobSite.is(jobSite), acquirableJobSite -> acquirableJobSite.is(jobSite), ImmutableSet.of(), ImmutableSet.of(), workSound));
    }
}
