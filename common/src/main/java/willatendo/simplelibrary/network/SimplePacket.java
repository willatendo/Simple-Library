package willatendo.simplelibrary.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface SimplePacket extends CustomPacketPayload {
    void encode(FriendlyByteBuf friendlyByteBuf);
}
