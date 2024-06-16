package willatendo.simplelibrary.client.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ClientEventsHolder {
    private final List<MenuScreenEntry> menuScreens = new ArrayList<MenuScreenEntry>();
    private final List<ModelLayerEntry> modelLayers = new ArrayList<ModelLayerEntry>();
    private final List<EntityModelEntry> entityModels = new ArrayList<EntityModelEntry>();
    private final List<BlockModelEntry> blockModels = new ArrayList<BlockModelEntry>();
    private final List<ParticleEntry> particleSheets = new ArrayList<ParticleEntry>();
    private final List<SkyRendererEntry> skyRenderers = new ArrayList<SkyRendererEntry>();

    public ClientEventsHolder() {
    }

    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void addMenuScreen(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor) {
        this.menuScreens.add(new MenuScreenEntry(menuType, screenConstructor));
    }

    public void addModelLayer(ModelLayerLocation modelLayerLocation, TexturedModelDataProvider texturedModelDataProvider) {
        this.modelLayers.add(new ModelLayerEntry(modelLayerLocation, texturedModelDataProvider));
    }

    public <T extends Entity> void addModel(EntityType<? extends T> entityType, EntityRendererProvider<? extends T> entityRendererProvider) {
        this.entityModels.add(new EntityModelEntry(entityType, entityRendererProvider));
    }

    public <T extends BlockEntity> void addModel(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<? extends T> blockEntityRendererProvider) {
        this.blockModels.add(new BlockModelEntry(blockEntityType, blockEntityRendererProvider));
    }

    public <T extends ParticleOptions> void addParticleSheet(ParticleType<? extends T> particleType, ParticleEngine.SpriteParticleRegistration<? extends T> particleSheetProvider) {
        this.particleSheets.add(new ParticleEntry(particleType, particleSheetProvider));
    }

    public void addSkyRenderer(ResourceKey<Level> levelResourceKey, SkyRendererProvider skyRendererProvider) {
        this.skyRenderers.add(new SkyRendererEntry(levelResourceKey, skyRendererProvider));
    }

    public void registerAllMenuScreens(Consumer<? super MenuScreenEntry> action) {
        this.menuScreens.forEach(action);
    }

    public void registerAllModelLayers(Consumer<? super ModelLayerEntry> action) {
        this.modelLayers.forEach(action);
    }

    public void registerAllEntityModels(Consumer<? super EntityModelEntry> action) {
        this.entityModels.forEach(action);
    }

    public void registerAllBlockModels(Consumer<? super BlockModelEntry> action) {
        this.blockModels.forEach(action);
    }

    public void registerAllParticleSheets(Consumer<? super ParticleEntry> action) {
        this.particleSheets.forEach(action);
    }

    public void registerAllSkyRenderers(Consumer<? super SkyRendererEntry> action) {
        this.skyRenderers.forEach(action);
    }
}
