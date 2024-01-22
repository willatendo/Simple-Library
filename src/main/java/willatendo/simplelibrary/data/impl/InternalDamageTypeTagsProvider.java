package willatendo.simplelibrary.data.impl;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import willatendo.simplelibrary.data.tags.SimpleDamageTypeTagsProvider;
import willatendo.simplelibrary.data.util.ExistingFileHelper;
import willatendo.simplelibrary.server.entity.damage.ForgeDamageTypeTags;
import willatendo.simplelibrary.server.entity.damage.SimpleDamageTypes;

public class InternalDamageTypeTagsProvider extends SimpleDamageTypeTagsProvider {
	public InternalDamageTypeTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, provider, modId, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
		this.tag(SimpleDamageTypes.POISON_DAMAGE, ForgeDamageTypeTags.IS_POISON);

		this.tag(DamageTypes.WITHER, ForgeDamageTypeTags.IS_WITHER);
		this.tag(DamageTypes.WITHER_SKULL, ForgeDamageTypeTags.IS_WITHER);

		this.tag(DamageTypes.MAGIC, ForgeDamageTypeTags.IS_MAGIC);
		this.tag(DamageTypes.INDIRECT_MAGIC, ForgeDamageTypeTags.IS_MAGIC);
		this.tag(DamageTypes.THORNS, ForgeDamageTypeTags.IS_MAGIC);
		this.tag(DamageTypes.DRAGON_BREATH, ForgeDamageTypeTags.IS_MAGIC);
		this.tag(ForgeDamageTypeTags.IS_MAGIC).addTags(ForgeDamageTypeTags.IS_POISON, ForgeDamageTypeTags.IS_WITHER);

		this.tag(DamageTypes.IN_FIRE, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.ON_FIRE, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.LAVA, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.HOT_FLOOR, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.DROWN, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.STARVE, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.DRY_OUT, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.FREEZE, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.LIGHTNING_BOLT, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.CACTUS, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.STALAGMITE, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.FALLING_STALACTITE, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.FALLING_BLOCK, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.FALLING_ANVIL, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.CRAMMING, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.FLY_INTO_WALL, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.SWEET_BERRY_BUSH, ForgeDamageTypeTags.IS_ENVIRONMENT);
		this.tag(DamageTypes.IN_WALL, ForgeDamageTypeTags.IS_ENVIRONMENT);

		this.tag(DamageTypes.CACTUS, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.STALAGMITE, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.FALLING_STALACTITE, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.FALLING_BLOCK, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.FALLING_ANVIL, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.CRAMMING, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.FLY_INTO_WALL, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.SWEET_BERRY_BUSH, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.FALL, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.STING, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.MOB_ATTACK, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.PLAYER_ATTACK, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.MOB_ATTACK_NO_AGGRO, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.ARROW, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.THROWN, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.TRIDENT, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.MOB_PROJECTILE, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.SONIC_BOOM, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.IN_WALL, ForgeDamageTypeTags.IS_PHYSICAL);
		this.tag(DamageTypes.GENERIC, ForgeDamageTypeTags.IS_PHYSICAL);

		this.tag(DamageTypes.GENERIC_KILL, ForgeDamageTypeTags.IS_TECHNICAL);
		this.tag(DamageTypes.OUTSIDE_BORDER, ForgeDamageTypeTags.IS_TECHNICAL);
		this.tag(DamageTypes.FELL_OUT_OF_WORLD, ForgeDamageTypeTags.IS_TECHNICAL);
	}

	private void tag(ResourceKey<DamageType> type, TagKey<DamageType>... tags) {
		for (TagKey<DamageType> key : tags) {
			this.tag(key).add(type);
		}
	}
}
