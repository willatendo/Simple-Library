package willatendo.simplelibrary.server.event.registry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import willatendo.simplelibrary.network.PacketSupplier;

public interface ServerboundPacketRegister {
    <T extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier);
}
