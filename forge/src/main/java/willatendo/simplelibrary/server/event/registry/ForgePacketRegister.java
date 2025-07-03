package willatendo.simplelibrary.server.event.registry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;
import willatendo.simplelibrary.client.event.registry.ClientToServerPacketRegister;
import willatendo.simplelibrary.network.PacketSupplier;
import willatendo.simplelibrary.network.SimplePacket;
import willatendo.simplelibrary.network.util.ForgeDecoder;
import willatendo.simplelibrary.network.util.ForgeEncoder;

public final class ForgePacketRegister implements ClientToServerPacketRegister, ServerToClientPacketRegister {
    private final SimpleChannel simpleChannel;

    public ForgePacketRegister(ResourceLocation id) {
        this.simpleChannel = ChannelBuilder.named(id).serverAcceptedVersions((status, version) -> true).clientAcceptedVersions((status, version) -> true).networkProtocolVersion(1).simpleChannel();
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientToServer(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier) {
    }

    @Override
    public <T extends SimplePacket> void registerForgeClientToServer(Class<T> simplePacket, ForgeEncoder forgeEncoder, ForgeDecoder forgeDecoder, PacketSupplier<T> packetSupplier) {
        this.simpleChannel.messageBuilder(simplePacket, NetworkDirection.PLAY_TO_SERVER).encoder((simplePacketIn, registryFriendlyByteBuf) -> forgeEncoder.encode(registryFriendlyByteBuf)).decoder(forgeDecoder::decode).consumerMainThread((simplePacketIn, context) -> context.enqueueWork(() -> packetSupplier.handle(simplePacketIn, context.getSender())));
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerToClient(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, PacketSupplier<T> packetSupplier) {
    }

    @Override
    public <T extends SimplePacket> void registerForgeServerToClient(Class<T> simplePacket, ForgeEncoder forgeEncoder, ForgeDecoder forgeDecoder, PacketSupplier<T> packetSupplier) {
        this.simpleChannel.messageBuilder(simplePacket, NetworkDirection.PLAY_TO_CLIENT).encoder((simplePacketIn, registryFriendlyByteBuf) -> forgeEncoder.encode(registryFriendlyByteBuf)).decoder(forgeDecoder::decode).consumerMainThread((simplePacketIn, context) -> context.enqueueWork(() -> packetSupplier.handle(simplePacketIn, context.getSender())));
    }
}
