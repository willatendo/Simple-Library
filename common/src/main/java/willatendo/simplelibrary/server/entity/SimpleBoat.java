package willatendo.simplelibrary.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import willatendo.simplelibrary.server.SimpleBuiltInRegistries;
import willatendo.simplelibrary.server.SimpleRegistries;
import willatendo.simplelibrary.server.entity.util.BoatTypeAccessor;
import willatendo.simplelibrary.server.entity.variant.BoatType;

import java.util.Objects;
import java.util.Optional;

public class SimpleBoat extends Boat implements BoatTypeAccessor {
    private static final EntityDataAccessor<Holder<BoatType>> BOAT_TYPE = SynchedEntityData.defineId(SimpleBoat.class, SimpleEntityDataSerializers.BOAT_TYPES.get());

    public SimpleBoat(EntityType<? extends SimpleBoat> entityType, Level level) {
        super(entityType, level);
    }

    public SimpleBoat(Level level, double x, double y, double z) {
        this(SimpleEntityTypes.SIMPLE_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public Item getDropItem() {
        return this.getBoatType().value().boat().value();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BOAT_TYPE, SimpleBuiltInRegistries.BOAT_TYPES.getHolderOrThrow(SimpleBoatTypes.EXAMPLE.getKey()));
    }

    @Override
    public Holder<BoatType> getBoatType() {
        return this.entityData.get(BOAT_TYPE);
    }

    @Override
    public void setBoatType(Holder<BoatType> boatType) {
        this.entityData.set(BOAT_TYPE, boatType);
    }

    @Override
    public void setVariant(Type type) {
        return;
    }

    @Override
    public Type getVariant() {
        return Type.OAK;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString("Variant", this.getBoatType().unwrapKey().orElse(SimpleBoatTypes.EXAMPLE.getKey()).location().toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);

        Optional<ResourceKey<BoatType>> boatType = Optional.ofNullable(ResourceLocation.tryParse(compoundTag.getString("Variant"))).map((resourceLocation) -> ResourceKey.create(SimpleRegistries.BOAT_TYPES, resourceLocation));
        Registry<BoatType> registry = SimpleBuiltInRegistries.BOAT_TYPES;
        Objects.requireNonNull(registry);
        boatType.flatMap(registry::getHolder).ifPresent(this::setBoatType);
    }

    @Override
    protected void checkFallDamage(double distance, boolean climbing, BlockState blockState, BlockPos blockPos) {
        this.lastYd = this.getDeltaMovement().y;
        if (!this.isPassenger()) {
            if (climbing) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != Boat.Status.ON_LAND) {
                        this.resetFallDistance();
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F, this.damageSources().fall());
                    if (!this.level().isClientSide && !this.isRemoved()) {
                        this.kill();
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (int i = 0; i < 3; ++i) {
                                this.spawnAtLocation(this.getBoatType().value().block().value());
                            }

                            for (int i = 0; i < 2; ++i) {
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                this.resetFallDistance();
            } else if (!this.level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && distance < 0.0) {
                this.fallDistance -= (float) distance;
            }
        }
    }
}
