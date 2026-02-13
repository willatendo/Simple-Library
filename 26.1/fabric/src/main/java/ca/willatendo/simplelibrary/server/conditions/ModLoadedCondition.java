package ca.willatendo.simplelibrary.server.conditions;

import ca.willatendo.simplelibrary.platform.utils.PlatformUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// Modified from Neoforge
public record ModLoadedCondition(String modid) implements ICondition {
    public static MapCodec<ModLoadedCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(Codec.STRING.fieldOf("modid").forGetter(ModLoadedCondition::modid)).apply(builder, ModLoadedCondition::new));

    @Override
    public boolean test(IContext context) {
        return PlatformUtils.isModLoaded(this.modid);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "mod_loaded(\"" + this.modid + "\")";
    }
}
