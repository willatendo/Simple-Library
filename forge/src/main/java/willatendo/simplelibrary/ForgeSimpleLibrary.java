package willatendo.simplelibrary;

import net.minecraftforge.fml.common.Mod;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod(SimpleUtils.SIMPLE_ID)
public class ForgeSimpleLibrary {
    public ForgeSimpleLibrary() {
        SimpleLibrary.onInitialize();
    }
}
