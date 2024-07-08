package willatendo.simplelibrary.server.entity;

import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import willatendo.simplelibrary.platform.ModloaderHelper;
import willatendo.simplelibrary.server.SimpleRegistries;
import willatendo.simplelibrary.server.entity.variant.BoatType;

import java.util.function.Supplier;

public class SimpleEntityDataSerializers {
    public static final Supplier<EntityDataSerializer<Holder<BoatType>>> BOAT_TYPES = ModloaderHelper.INSTANCE.registerDataSerializer("boat_types", ByteBufCodecs.holderRegistry(SimpleRegistries.BOAT_TYPES));

    public static void init() {
    }
}
