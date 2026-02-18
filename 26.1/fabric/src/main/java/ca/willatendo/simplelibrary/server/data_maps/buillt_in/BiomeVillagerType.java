package ca.willatendo.simplelibrary.server.data_maps.buillt_in;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerType;

public record BiomeVillagerType(ResourceKey<VillagerType> type) {
    public static final Codec<BiomeVillagerType> TYPE_CODEC = ResourceKey.codec(Registries.VILLAGER_TYPE).xmap(BiomeVillagerType::new, BiomeVillagerType::type);
    public static final Codec<BiomeVillagerType> CODEC = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(ResourceKey.codec(Registries.VILLAGER_TYPE).fieldOf("villager_type").forGetter(BiomeVillagerType::type)).apply(instance, BiomeVillagerType::new)), TYPE_CODEC);
}
