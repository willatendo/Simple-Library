package willatendo.simplelibrary.server.event.registry;

import net.minecraft.world.level.GameRules;

public final class NeoforgeGameRuleRegistry implements GameRuleRegistry {
    @Override
    public GameRules.Key<GameRules.BooleanValue> registerBoolean(String id, GameRules.Category category, boolean defaultValue) {
        return GameRules.register(id, category, GameRules.BooleanValue.create(defaultValue));
    }

    @Override
    public GameRules.Key<GameRules.IntegerValue> registerInteger(String id, GameRules.Category category, int defaultValue) {
        return GameRules.register(id, category, GameRules.IntegerValue.create(defaultValue));
    }
}
