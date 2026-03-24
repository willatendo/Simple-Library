package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;

import java.util.function.ToIntFunction;

public class GameRuleSubRegistry extends SimpleRegistry<GameRule<?>> {
    public GameRuleSubRegistry(String modId) {
        super(Registries.GAME_RULE, modId);
    }

    public SimpleHolder<GameRule<Integer>> registerInteger(String name, GameRuleCategory gameRuleCategory, int defaultValue, int min) {
        return this.registerInteger(name, gameRuleCategory, defaultValue, min, Integer.MAX_VALUE, FeatureFlagSet.of());
    }

    public SimpleHolder<GameRule<Integer>> registerInteger(String name, GameRuleCategory gameRuleCategory, int defaultValue, int min, int max) {
        return this.registerInteger(name, gameRuleCategory, defaultValue, min, max, FeatureFlagSet.of());
    }

    public SimpleHolder<GameRule<Integer>> registerInteger(String name, GameRuleCategory gameRuleCategory, int defaultValue, int min, int max, FeatureFlagSet featureFlagSet) {
        return this.register(name, gameRuleCategory, GameRuleType.INT, IntegerArgumentType.integer(min, max), Codec.intRange(min, max), defaultValue, featureFlagSet, GameRuleTypeVisitor::visitInteger, i -> i);
    }

    public SimpleHolder<GameRule<Boolean>> registerBoolean(String name, GameRuleCategory gameRuleCategory, boolean defaultValue) {
        return this.register(name, gameRuleCategory, GameRuleType.BOOL, BoolArgumentType.bool(), Codec.BOOL, defaultValue, FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, p_460985_ -> p_460985_ ? 1 : 0);
    }

    public <T> SimpleHolder<GameRule<T>> register(String name, GameRuleCategory gameRuleCategory, GameRuleType gameRuleType, ArgumentType<T> argumentType, Codec<T> codec, T defaultValue, FeatureFlagSet featureFlagSet, GameRules.VisitorCaller<T> visitorCaller, ToIntFunction<T> commandResultFunction) {
        return this.register(name, () -> new GameRule<>(gameRuleCategory, gameRuleType, argumentType, visitorCaller, codec, commandResultFunction, defaultValue, featureFlagSet));
    }
}
