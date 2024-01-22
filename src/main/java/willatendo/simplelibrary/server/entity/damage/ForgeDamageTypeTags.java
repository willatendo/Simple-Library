package willatendo.simplelibrary.server.entity.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import willatendo.simplelibrary.server.util.SimpleUtils;
import willatendo.simplelibrary.server.util.TagRegister;

public class ForgeDamageTypeTags {
	public static final TagRegister<DamageType> DAMAGE_TYPES = SimpleUtils.create(Registries.DAMAGE_TYPE, SimpleUtils.FORGE);

	public static final TagKey<DamageType> IS_MAGIC = DAMAGE_TYPES.register("is_magic");
	public static final TagKey<DamageType> IS_POISON = DAMAGE_TYPES.register("is_poison");
	public static final TagKey<DamageType> IS_WITHER = DAMAGE_TYPES.register("is_wither");
	public static final TagKey<DamageType> IS_ENVIRONMENT = DAMAGE_TYPES.register("is_environment");
	public static final TagKey<DamageType> IS_PHYSICAL = DAMAGE_TYPES.register("is_physical");
	public static final TagKey<DamageType> IS_TECHNICAL = DAMAGE_TYPES.register("is_technical");
}
