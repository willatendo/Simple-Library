package willatendo.simplelibrary.server.entity.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleDamageTypes {
	public static final ResourceKey<DamageType> POISON_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, SimpleUtils.resource("poison"));

	public static void bootstrap(BootstapContext<DamageType> bootstapContext) {
		bootstapContext.register(POISON_DAMAGE, new DamageType("magic", 0.0F));
	}
}
