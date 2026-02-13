package ca.willatendo.simplelibrary.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface PacketRegistryListener {
    default void registerClientboundPackets(PacketRegistryListener.ClientboundPacketRegister clientboundPacketRegister) {
    }

    default void registerServerboundPackets(PacketRegistryListener.ServerboundPacketRegister serverboundPacketRegister) {
    }

    @FunctionalInterface
    interface ClientboundPacketRegister {
        <T extends CustomPacketPayload> void apply(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PacketSupplier<T> packetSupplier);
    }

    @FunctionalInterface
    interface ServerboundPacketRegister {
        <T extends CustomPacketPayload> void apply(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PacketSupplier<T> packetSupplier);
    }
}
