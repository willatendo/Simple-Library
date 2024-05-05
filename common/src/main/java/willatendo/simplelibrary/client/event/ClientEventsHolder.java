package willatendo.simplelibrary.client.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ClientEventsHolder {
	private final List<ModelLayerEntry> modelLayers = new ArrayList<ModelLayerEntry>();
	private final List<EntityModelEntry> entityModels = new ArrayList<EntityModelEntry>();
	private final List<BlockModelEntry> blockModels = new ArrayList<BlockModelEntry>();

	public ClientEventsHolder() {
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

	public void registerAllModelLayers(Consumer<? super ModelLayerEntry> action) {
		this.modelLayers.forEach(action);
	}

	public void registerAllEntityModels(Consumer<? super EntityModelEntry> action) {
		this.entityModels.forEach(action);
	}

	public void registerAllBlockModels(Consumer<? super BlockModelEntry> action) {
		this.blockModels.forEach(action);
	}
}
