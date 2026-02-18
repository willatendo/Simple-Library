package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.event.TagEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {
    @Shadow
    @Final
    private RegistryAccess.Frozen registryAccess;

    protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(at = @At("HEAD"), method = "handleUpdateTags")
    private void handleUpdateTags(ClientboundUpdateTagsPacket clientboundUpdateTagsPacket, CallbackInfo ci) {
        boolean flag = this.connection.isMemoryConnection();
        TagEvents.UPDATE_TAGS.invoker().update(this.registryAccess, true, flag);
    }
}
