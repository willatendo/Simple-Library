package willatendo.simplelibrary.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public interface PacketSupplier<T extends CustomPacketPayload> {
    void handle(T customPacketPayload, Player player);
}
