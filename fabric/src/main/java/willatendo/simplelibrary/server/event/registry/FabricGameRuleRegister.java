package willatendo.simplelibrary.server.event.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public final class FabricGameRuleRegister implements GameRuleRegister {
    @Override
    public GameRules.Key<GameRules.BooleanValue> registerBoolean(String id, GameRules.Category category, boolean defaultValue) {
        return GameRuleRegistry.register(id, category, GameRuleFactory.createBooleanRule(defaultValue));
    }

    @Override
    public GameRules.Key<GameRules.IntegerValue> registerInteger(String id, GameRules.Category category, int defaultValue) {
        return GameRuleRegistry.register(id, category, GameRuleFactory.createIntRule(defaultValue));
    }
}
