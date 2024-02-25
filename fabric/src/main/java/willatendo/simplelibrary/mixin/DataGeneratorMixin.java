package willatendo.simplelibrary.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

@Mixin(DataGenerator.class)
public interface DataGeneratorMixin {
	@Accessor("vanillaPackOutput")
	public PackOutput getVanillaPackOutput();
}
