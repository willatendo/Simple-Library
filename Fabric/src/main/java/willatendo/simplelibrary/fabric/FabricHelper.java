package willatendo.simplelibrary.fabric;

import java.nio.file.Path;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab.Builder;
import willatendo.simplelibrary.helper.ModloaderHelper;
import willatendo.simplelibrary.server.util.SimpleUtils.ExtendedFactory;

public class FabricHelper implements ModloaderHelper {
	@Override
	public Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public Builder getBuilder() {
		return FabricItemGroup.builder();
	}

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedFactory<T> extendedFactory) {
		return new ExtendedScreenHandlerType<>((windowId, inventory, friendlyByteBuffer) -> extendedFactory.create(windowId, inventory, friendlyByteBuffer));
	}

	@Override
	public <T> void register(Registry<T> registry, ResourceLocation id, Supplier<T> object) {
		Registry.register(registry, id, object.get());
	}
}
