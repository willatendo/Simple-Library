package ca.willatendo.simplelibrary.client.utils;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ClientUtils {
    private ClientUtils() {
    }

    public static void createSheetForWoodType(WoodType woodType) {
        Identifier identifier = Identifier.parse(woodType.name());
        Sheets.SIGN_MAPPER.apply(identifier);
        Sheets.HANGING_SIGN_MAPPER.apply(identifier);
    }
}
