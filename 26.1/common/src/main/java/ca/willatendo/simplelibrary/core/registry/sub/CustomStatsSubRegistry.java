package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import java.util.Map;

public class CustomStatsSubRegistry extends SimpleRegistry<Identifier> {
    private final Map<Identifier, StatFormatter> statFormatters = Maps.newHashMap();

    public CustomStatsSubRegistry(String modId) {
        super(Registries.CUSTOM_STAT, modId);
    }

    public void registerStatFormatters() {
        this.statFormatters.forEach(Stats.CUSTOM::get);
    }

    public Identifier registerCustomStat(String name, StatFormatter statFormatter) {
        Identifier statIdentifier = CoreUtils.resource(this.modId, name);
        this.register(name, () -> statIdentifier);
        this.statFormatters.put(statIdentifier, statFormatter);
        return statIdentifier;
    }
}
