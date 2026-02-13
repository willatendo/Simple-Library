package ca.willatendo.simplelibrary.network;

import ca.willatendo.simplelibrary.network.clientbound.ClientboundRecipeContentPacket;

public record FabricSimpleLibraryPacketRegistryListener() implements PacketRegistryListener {
    @Override
    public void registerClientboundPackets(ClientboundPacketRegister clientboundPacketRegister) {
        clientboundPacketRegister.apply(ClientboundRecipeContentPacket.TYPE, ClientboundRecipeContentPacket.STREAM_CODEC, FabricSimpleClientBoundPackets::clientboundRecipeContentPacket);
    }
}
