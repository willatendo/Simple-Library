package willatendo.simplelibrary.client.util;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ClientUtils {
    public static void registerWoodType(WoodType woodType) {
        ResourceLocation resourceLocation = ResourceLocation.parse(woodType.name());
        Sheets.SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), "entity/signs/" + resourceLocation.getPath())));
        Sheets.HANGING_SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), "entity/signs/hanging/" + resourceLocation.getPath())));
    }
}
