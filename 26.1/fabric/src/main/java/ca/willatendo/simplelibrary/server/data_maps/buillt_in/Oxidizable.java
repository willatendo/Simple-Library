package ca.willatendo.simplelibrary.server.data_maps.buillt_in;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public record Oxidizable(Block nextOxidationStage) {
    public static final Codec<Oxidizable> OXIDIZABLE_CODEC = BuiltInRegistries.BLOCK.byNameCodec().xmap(Oxidizable::new, Oxidizable::nextOxidationStage);
    public static final Codec<Oxidizable> CODEC = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("next_oxidation_stage").forGetter(Oxidizable::nextOxidationStage)).apply(instance, Oxidizable::new)), OXIDIZABLE_CODEC);
}
