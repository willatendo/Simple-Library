package ca.willatendo.simplelibrary.client;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ClientEventListener {
    default void clientSetup() {
    }

    // Registry

    default void registerBlockColors(ClientEventListener.BlockColorRegister blockColorRegister) {
    }

    default void registerKeyMappings(ClientEventListener.KeyMappingRegister keyMappingRegister) {
    }

    default void registerMenuScreens(ClientEventListener.MenuScreenRegister menuScreenRegister) {
    }

    default void registerLayerDefinitions(ClientEventListener.LayerDefinitionRegister layerDefinitionRegister) {
    }

    default void registerRenderers(ClientEventListener.RendererRegister rendererRegister) {
    }

    default void registerRecipeBookOverlay(ClientEventListener.RecipeBookOverlayRegister recipeBookOverlayRegister) {
    }

    default void registerParticleColorExemptions(ClientEventListener.ParticleColorExemptionRegister particleColorExemptionRegister) {
    }

    default void registerParticleProviders(ClientEventListener.ParticleProviderRegister particleProviderRegister) {
    }

    default void registerSpecialModelRenderers(ClientEventListener.SpecialModelRendererRegister specialModelRendererRegister) {
    }

    // Events

    default void screenInitPreEvent(Screen screen, Consumer<AbstractWidget> widgets) {
    }

    default void screenInitPostEvent(Screen screen, Consumer<AbstractWidget> widgets) {
    }

    default void screenRenderPreEvent(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    default void screenRenderPostEvent(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    default void screenCloseEvent(Screen screen) {
    }

    default void clientPlayerLoggingOut() {
    }

    @FunctionalInterface
    interface BlockColorRegister {
        void apply(BlockColor blockColor, Block... blocks);
    }

    @FunctionalInterface
    interface KeyMappingRegister {
        void apply(KeyMapping keyMapping);
    }

    @FunctionalInterface
    interface MenuScreenRegister {
        <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void apply(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor);
    }

    @FunctionalInterface
    interface LayerDefinitionRegister {
        void apply(ModelLayerLocation modelLayerLocation, Supplier<LayerDefinition> layerDefinition);
    }

    interface RendererRegister {
        <T extends Entity> void apply(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);

        <T extends BlockEntity, S extends BlockEntityRenderState> void apply(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T, S> blockEntityRendererProvider);
    }

    @FunctionalInterface
    interface RecipeBookOverlayRegister {
        void apply(Class<? extends RecipeBookComponent<?>> clazz, Pair<BiFunction<RecipeDisplay, ContextMap, List<OverlayRecipeComponent.OverlayRecipeButton.Pos>>, BiFunction<Boolean, Boolean, Identifier>> pair);
    }

    @FunctionalInterface
    interface ParticleColorExemptionRegister {
        void apply(Block... blocks);
    }

    interface ParticleProviderRegister {
        <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleProvider<T> particleFactory);

        <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite);

        <T extends ParticleOptions> void apply(ParticleType<T> particleType, ParticleResources.SpriteParticleRegistration<T> particleMetaFactory);
    }

    @FunctionalInterface
    interface SpecialModelRendererRegister {
        void apply(Identifier id, MapCodec<? extends SpecialModelRenderer.Unbaked> codec);
    }
}
