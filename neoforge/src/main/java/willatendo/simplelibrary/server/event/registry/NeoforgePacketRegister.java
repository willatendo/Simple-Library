package willatendo.simplelibrary.server.event.registry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import willatendo.simplelibrary.client.event.registry.ClientToServerPacketRegister;
import willatendo.simplelibrary.network.PacketSupplier;

public final class NeoforgePacketRegister implements ClientToServerPacketRegister, ServerToClientPacketRegister {
    private final PayloadRegistrar payloadRegistrar;

    public NeoforgePacketRegister(RegisterPayloadHandlersEvent event, String modId, String version) {
        this.payloadRegistrar = event.registrar(modId).versioned(version).optional();
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientToServer(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier) {
        this.payloadRegistrar.playToClient(type, reader, (customPacketPayload, iPayloadContext) -> iPayloadContext.enqueueWork(() -> packetSupplier.handle(customPacketPayload, iPayloadContext.player())));
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerToClient(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier) {
        this.payloadRegistrar.playToServer(type, reader, (customPacketPayload, iPayloadContext) -> iPayloadContext.enqueueWork(() -> packetSupplier.handle(customPacketPayload, iPayloadContext.player())));
    }
}
