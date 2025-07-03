package willatendo.simplelibrary.server.event.registry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import willatendo.simplelibrary.network.PacketSupplier;
import willatendo.simplelibrary.network.util.ForgeDecoder;

import java.util.function.BiConsumer;

public interface ServerToClientPacketRegister {
    <T extends CustomPacketPayload> void registerServerToClient(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier);

    default <T extends CustomPacketPayload> void registerForgeServerToClient(Class<T> customPacketPayload, BiConsumer<T, RegistryFriendlyByteBuf> encoder, ForgeDecoder<T> forgeDecoder, PacketSupplier<T> packetSupplier) {
    }

    default <T extends CustomPacketPayload> void registerServerToClient(Class<T> customPacketPayload, CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, BiConsumer<T, RegistryFriendlyByteBuf> encoder, ForgeDecoder<T> forgeDecoder, PacketSupplier<T> packetSupplier) {
        this.registerServerToClient(type, reader, packetSupplier);
        this.registerForgeServerToClient(customPacketPayload, encoder, forgeDecoder, packetSupplier);
    }
}
