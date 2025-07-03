package willatendo.simplelibrary.network.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import willatendo.simplelibrary.network.SimplePacket;

public interface ForgeEncoder {
    <T>void encode(FriendlyByteBuf friendlyByteBuf);
}
