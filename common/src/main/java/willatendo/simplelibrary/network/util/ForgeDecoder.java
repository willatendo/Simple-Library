package willatendo.simplelibrary.network.util;

import net.minecraft.network.FriendlyByteBuf;
import willatendo.simplelibrary.network.SimplePacket;

public interface ForgeDecoder {
    <T extends SimplePacket> T decode(FriendlyByteBuf friendlyByteBuf);
}
