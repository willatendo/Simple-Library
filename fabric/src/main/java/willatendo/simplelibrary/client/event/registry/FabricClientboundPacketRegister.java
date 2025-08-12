package willatendo.simplelibrary.client.event.registry;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import willatendo.simplelibrary.network.PacketSupplier;

public final class FabricClientboundPacketRegister implements ClientboundPacketRegister {
    @Override
    public <T extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier) {
        PayloadTypeRegistry.playS2C().register(type, reader);

        ClientPlayNetworking.registerGlobalReceiver(type, (customPacketPayload, context) -> packetSupplier.handle(customPacketPayload, context.player()));
    }
}
