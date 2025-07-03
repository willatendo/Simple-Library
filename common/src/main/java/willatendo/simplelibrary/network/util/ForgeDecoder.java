package willatendo.simplelibrary.network.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ForgeDecoder<T extends CustomPacketPayload> {
    T decode(FriendlyByteBuf friendlyByteBuf);
}
