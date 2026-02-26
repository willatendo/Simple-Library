package ca.willatendo.simplelibrary.network.clientbound;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.server.stats.CustomRecipeBookSettings;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.stats.RecipeBookSettings;

public record ClientboundCustomRecipeBookSettingsPacket(RecipeBookSettings recipeBookSettings, CustomRecipeBookSettings customRecipeBookSettings) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundCustomRecipeBookSettingsPacket> TYPE = new CustomPacketPayload.Type<>(SimpleCoreUtils.resource("custom_recipe_book_settings"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCustomRecipeBookSettingsPacket> STREAM_CODEC = StreamCodec.composite(RecipeBookSettings.STREAM_CODEC, ClientboundCustomRecipeBookSettingsPacket::recipeBookSettings, CustomRecipeBookSettings.STREAM_CODEC, ClientboundCustomRecipeBookSettingsPacket::customRecipeBookSettings, ClientboundCustomRecipeBookSettingsPacket::new);

    @Override
    public CustomPacketPayload.Type<ClientboundCustomRecipeBookSettingsPacket> type() {
        return TYPE;
    }
}
