package ca.willatendo.simplelibrary.platform;

import ca.willatendo.simplelibrary.core.registry.SimpleRegistryBuilder;
import ca.willatendo.simplelibrary.platform.utils.PlatformUtils;
import ca.willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public interface SimpleLibraryPlatformHelper {
    SimpleLibraryPlatformHelper INSTANCE = PlatformUtils.ofPlatformHelper(SimpleLibraryPlatformHelper.class);

    Platform getPlatform();

    void sendToServer(CustomPacketPayload customPacketPayload, CustomPacketPayload... customPacketPayloads);

    void sendToClient(ServerPlayer serverPlayer, CustomPacketPayload customPacketPayload);

    FeatureFlag getFeatureFlag(Identifier identifier);

    RecipeBookType getRecipeBookType(String modId, String name);

    <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder);

    SimpleParticleType createSimpleParticleType(boolean overrideLimiter);

    <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier);

    CreativeModeTab.Builder createCreativeModeTab();

    void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer);

    void registerRecipeBookOverlayEvent(Map<Class<? extends RecipeBookComponent<?>>, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>>> map);
}
