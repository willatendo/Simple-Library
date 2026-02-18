package ca.willatendo.simplelibrary.client;

import ca.willatendo.simplelibrary.client.filter.CreativeModeTabFilter;
import ca.willatendo.simplelibrary.client.filter.Filter;
import ca.willatendo.simplelibrary.network.FabricSimpleLibraryPacketRegistryListener;
import ca.willatendo.simplelibrary.platform.utils.PlatformUtils;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class FabricSimpleLibraryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (PlatformUtils.isDevelopmentalEnvironment()) {
            ImmutableList.Builder<Filter> itemFilters = ImmutableList.builder();
            itemFilters.add(new Filter(ItemTags.OAK_LOGS, new ItemStack(Items.OAK_LOG)));
            itemFilters.add(new Filter(ItemTags.SPRUCE_LOGS, new ItemStack(Items.SPRUCE_LOG)));
            itemFilters.add(new Filter(ItemTags.BIRCH_LOGS, new ItemStack(Items.BIRCH_LOG)));
            itemFilters.add(new Filter(ItemTags.JUNGLE_LOGS, new ItemStack(Items.JUNGLE_LOG)));
            itemFilters.add(new Filter(ItemTags.ACACIA_LOGS, new ItemStack(Items.ACACIA_LOG)));
            itemFilters.add(new Filter(ItemTags.DARK_OAK_LOGS, new ItemStack(Items.DARK_OAK_LOG)));
            CreativeModeTabFilter.create(BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("building_blocks"))).value(), itemFilters);
        }

        FabricClientModInit fabricClientModInit = new FabricClientModInit();
        fabricClientModInit.packetRegistryListener(new FabricSimpleLibraryPacketRegistryListener());
        fabricClientModInit.clientEventListener(new SimpleLibraryClientEventListener());
    }
}
