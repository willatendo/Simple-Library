package willatendo.simplelibrary.network.util;

import net.minecraft.network.FriendlyByteBuf;

public interface ForgeEncoder {
    void encode(FriendlyByteBuf friendlyByteBuf);
}
