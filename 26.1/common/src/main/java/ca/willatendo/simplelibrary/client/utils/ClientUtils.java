package ca.willatendo.simplelibrary.client.utils;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ClientUtils {
    private ClientUtils() {
    }

    public static void createSheetForWoodType(WoodType woodType) {
        Identifier identifier = Identifier.parse(woodType.name());
        Sheets.SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, Identifier.fromNamespaceAndPath(identifier.getNamespace(), "entity/signs/" + identifier.getPath())));
        Sheets.HANGING_SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, Identifier.fromNamespaceAndPath(identifier.getNamespace(), "entity/signs/hanging/" + identifier.getPath())));
    }
}
