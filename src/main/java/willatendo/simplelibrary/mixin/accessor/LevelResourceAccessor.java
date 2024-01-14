package willatendo.simplelibrary.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.level.storage.LevelResource;

@Mixin(LevelResource.class)
public interface LevelResourceAccessor {
	@Invoker("<init>")
	static LevelResource config$create(String string) {
		throw new IllegalStateException();
	}
}
