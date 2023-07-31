package willatendo.simplelibrary.data;

import java.util.Objects;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class SimpleItemModelProvider extends ItemModelProvider {
	public SimpleItemModelProvider(PackOutput packOutput, String modid, ExistingFileHelper existingFileHelper) {
		super(packOutput, modid, existingFileHelper);
	}

	public ItemModelBuilder handheldItem(Item item) {
		return this.handheldItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)), new ResourceLocation(ForgeRegistries.ITEMS.getKey(item).getNamespace(), "item/" + ForgeRegistries.ITEMS.getKey(item).getPath()));
	}

	public ItemModelBuilder handheldItem(Item item, ResourceLocation texture) {
		return this.handheldItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)), texture);
	}

	public ItemModelBuilder basicItem(Item item, ResourceLocation texture) {
		return this.basicItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)), texture);
	}

	public ItemModelBuilder basicItem(ResourceLocation item, ResourceLocation texture) {
		return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", texture);
	}

	public ItemModelBuilder handheldItem(ResourceLocation item, ResourceLocation texture) {
		return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", texture);
	}

	public ItemModelBuilder spawnEggItem(Item item) {
		return this.spawnEggItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
	}

	public ItemModelBuilder spawnEggItem(ResourceLocation item) {
		return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
	}

	public void basicBlock(Block block) {
		this.getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath()).parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath())));
	}

	@Override
	public String getName() {
		return this.modid + ": Item Models";
	}
}
