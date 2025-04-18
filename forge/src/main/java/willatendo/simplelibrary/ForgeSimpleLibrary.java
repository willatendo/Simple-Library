package willatendo.simplelibrary;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import willatendo.simplelibrary.server.event.modification.ForgeOxidationModification;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod(SimpleUtils.SIMPLE_ID)
public class ForgeSimpleLibrary {
    public ForgeSimpleLibrary() {
        SimpleLibrary.onInitialize();

        var a = new ForgeOxidationModification();
        a.add(Blocks.CHAIN, Blocks.ACACIA_PLANKS);
        a.build();
    }
}
