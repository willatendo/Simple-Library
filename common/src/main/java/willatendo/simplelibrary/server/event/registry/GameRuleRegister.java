package willatendo.simplelibrary.server.event.registry;

import net.minecraft.world.level.GameRules;

public interface GameRuleRegister {
    GameRules.Key<GameRules.BooleanValue> registerBoolean(String id, GameRules.Category category, boolean defaultValue);

    GameRules.Key<GameRules.IntegerValue> registerInteger(String id, GameRules.Category category, int defaultValue);
}
