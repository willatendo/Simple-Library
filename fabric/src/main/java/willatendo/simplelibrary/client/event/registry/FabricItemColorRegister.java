package willatendo.simplelibrary.client.event.registry;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;

public final class FabricItemColorRegister implements ItemColorRegistry {
    @Override
    public BlockColors getBlockColors() {
        return Minecraft.getInstance().getBlockColors();
    }

    @Override
    public void registerItemColor(ItemColor itemColor, ItemLike... items) {
        ColorProviderRegistry.ITEM.register(itemColor, items);
    }
}
