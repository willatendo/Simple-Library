package willatendo.simplelibrary.client.event.registry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import willatendo.simplelibrary.network.PacketSupplier;
import willatendo.simplelibrary.network.util.ForgeDecoder;
import willatendo.simplelibrary.network.util.ForgeEncoder;

public interface ClientToServerPacketRegister {
    <T extends CustomPacketPayload> void registerClientToServer(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier);

    default <T extends CustomPacketPayload> void registerForgeClientToServer(Class<T> customPacketPayload, ForgeEncoder forgeEncoder, ForgeDecoder<T> forgeDecoder, PacketSupplier<T> packetSupplier) {
    }
}
