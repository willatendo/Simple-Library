package ca.willatendo.simplelibrary.network.clientbound;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record ClientboundRecipeContentPacket(Set<RecipeType<?>> recipeTypes, List<RecipeHolder<?>> recipes) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundRecipeContentPacket> TYPE = new CustomPacketPayload.Type<>(SimpleCoreUtils.resource("recipe_content"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRecipeContentPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(Registries.RECIPE_TYPE).apply(ByteBufCodecs.collection(HashSet::new)), ClientboundRecipeContentPacket::recipeTypes, RecipeHolder.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundRecipeContentPacket::recipes, ClientboundRecipeContentPacket::new);

    public static ClientboundRecipeContentPacket create(Collection<RecipeType<?>> recipeTypes, RecipeMap recipeMap) {
        Set<RecipeType<?>> recipeTypesSet = Set.copyOf(recipeTypes);
        if (recipeTypesSet.isEmpty()) {
            return new ClientboundRecipeContentPacket(recipeTypesSet, List.of());
        } else {
            List<RecipeHolder<?>> recipeSubset = recipeMap.values().stream().filter(recipeHolder -> recipeTypesSet.contains(recipeHolder.value().getType())).toList();
            return new ClientboundRecipeContentPacket(recipeTypesSet, recipeSubset);
        }
    }

    @Override
    public CustomPacketPayload.Type<ClientboundRecipeContentPacket> type() {
        return TYPE;
    }
}