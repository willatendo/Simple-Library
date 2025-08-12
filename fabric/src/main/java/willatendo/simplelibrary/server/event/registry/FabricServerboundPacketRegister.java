package willatendo.simplelibrary.server.event.registry;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import willatendo.simplelibrary.network.PacketSupplier;

public final class FabricServerboundPacketRegister implements ServerboundPacketRegister {
    @Override
    public <T extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier) {
        PayloadTypeRegistry.playC2S().register(type, reader);

        ServerPlayNetworking.registerGlobalReceiver(type, (customPacketPayload, context) -> packetSupplier.handle(customPacketPayload, context.player()));
    }
}
