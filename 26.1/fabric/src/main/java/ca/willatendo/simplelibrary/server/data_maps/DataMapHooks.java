package ca.willatendo.simplelibrary.server.data_maps;

import ca.willatendo.simplelibrary.server.data_maps.buillt_in.FurnaceFuel;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.Oxidizable;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.Waxable;
import ca.willatendo.simplelibrary.server.event.DataMapEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class DataMapHooks {
    public static boolean didHaveToFallbackToVanillaMaps = false;
    private static final Map<Block, Block> INVERSE_OXIDIZABLES_DATAMAP_INTERNAL = new HashMap<>();
    private static final Map<Block, Block> INVERSE_WAXABLES_DATAMAP_INTERNAL = new HashMap<>();
    public static final Map<Block, Block> INVERSE_OXIDIZABLES_DATAMAP = Collections.unmodifiableMap(INVERSE_OXIDIZABLES_DATAMAP_INTERNAL);
    public static final Map<Block, Block> INVERSE_WAXABLES_DATAMAP = Collections.unmodifiableMap(INVERSE_WAXABLES_DATAMAP_INTERNAL);

    public static Block getNextOxidizedStage(Block block) {
        Oxidizable oxidizable = (Oxidizable) block.builtInRegistryHolder().getData(SimpleLibraryDataMaps.OXIDIZABLES);
        return oxidizable != null ? oxidizable.nextOxidationStage() : WeatheringCopper.NEXT_BY_BLOCK.get().get(block);
    }

    public static Block getPreviousOxidizedStage(Block block) {
        return INVERSE_OXIDIZABLES_DATAMAP.containsKey(block) ? INVERSE_OXIDIZABLES_DATAMAP.get(block) : WeatheringCopper.PREVIOUS_BY_BLOCK.get().get(block);
    }

    public static Block getBlockWaxed(Block block) {
        Waxable waxable = (Waxable) block.builtInRegistryHolder().getData(SimpleLibraryDataMaps.WAXABLES);
        return waxable != null ? waxable.waxed() : HoneycombItem.WAXABLES.get().get(block);
    }

    public static Block getBlockUnwaxed(Block block) {
        return INVERSE_WAXABLES_DATAMAP.containsKey(block) ? INVERSE_WAXABLES_DATAMAP.get(block) : HoneycombItem.WAX_OFF_BY_BLOCK.get().get(block);
    }

    public static FuelValues populateFuelValues(RegistryAccess lookupProvider, FeatureFlagSet features) {
        FuelValues.Builder builder = new FuelValues.Builder(lookupProvider, features);
        Registry<Item> registry = lookupProvider.lookupOrThrow(Registries.ITEM);
        registry.getDataMap(SimpleLibraryDataMaps.FURNACE_FUELS).forEach((key, fuel) -> builder.add(registry.getValue((ResourceKey<Item>) key), ((FurnaceFuel) fuel).burnTime()));
        return builder.build();
    }

    public static void init() {
        DataMapEvents.UPDATE_DATA_MAPS.register((registryAccess, registry, updateCause) -> {
            if (registry.key() == Registries.BLOCK) {
                INVERSE_OXIDIZABLES_DATAMAP_INTERNAL.clear();
                INVERSE_WAXABLES_DATAMAP_INTERNAL.clear();

                registry.getDataMap(SimpleLibraryDataMaps.OXIDIZABLES).forEach((resourceKey, oxidizable) -> {
                    var block = BuiltInRegistries.BLOCK.getValue((ResourceKey<Block>) resourceKey);

                    INVERSE_OXIDIZABLES_DATAMAP_INTERNAL.put(((Oxidizable) oxidizable).nextOxidationStage(), block);

                    for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                        state.initCache();
                    }
                });

                WeatheringCopper.PREVIOUS_BY_BLOCK.get().forEach((after, before) -> {
                    if (!INVERSE_OXIDIZABLES_DATAMAP_INTERNAL.containsKey(after)) {
                        INVERSE_OXIDIZABLES_DATAMAP_INTERNAL.put(after, before);
                        didHaveToFallbackToVanillaMaps = true;
                    }
                });

                registry.getDataMap(SimpleLibraryDataMaps.WAXABLES).forEach((resourceKey, waxable) -> INVERSE_WAXABLES_DATAMAP_INTERNAL.put(((Waxable) waxable).waxed(), BuiltInRegistries.BLOCK.getValue((ResourceKey<Block>) resourceKey)));

                HoneycombItem.WAX_OFF_BY_BLOCK.get().forEach((after, before) -> {
                    if (!INVERSE_WAXABLES_DATAMAP_INTERNAL.containsKey(after)) {
                        INVERSE_OXIDIZABLES_DATAMAP_INTERNAL.put(after, before);
                        didHaveToFallbackToVanillaMaps = true;
                    }
                });
            }
        });
    }
}
