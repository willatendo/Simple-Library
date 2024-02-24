package willatendo.simplelibrary.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

@Mixin(Pack.class)
public interface PackMixin {
	@Invoker("<init>")
	public static Pack create(boolean shouldRun, String name, FabricDataOutput output) {
		throw new AssertionError();
	}
}
