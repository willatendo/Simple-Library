package willatendo.simplelibrary.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class SimpleBlockStateProvider extends BlockStateProvider {
    public SimpleBlockStateProvider(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
    }

    public void woodBlock(RotatedPillarBlock block, ResourceLocation texture) {
        this.axisBlock(block, texture, texture);
    }

    public void hangingSignBlock(CeilingHangingSignBlock ceilingHangingSignBlock, WallHangingSignBlock wallHangingSignBlock, ResourceLocation texture) {
        ModelFile sign = this.models().sign(this.name(ceilingHangingSignBlock), texture);
        this.hangingSignBlock(ceilingHangingSignBlock, wallHangingSignBlock, sign);
    }

    public void hangingSignBlock(CeilingHangingSignBlock ceilingHangingSignBlock, WallHangingSignBlock wallHangingSignBlock, ModelFile sign) {
        this.simpleBlock(ceilingHangingSignBlock, sign);
        this.simpleBlock(wallHangingSignBlock, sign);
    }

    public void fenceBlock(FenceBlock block, ResourceLocation texture) {
        String baseName = this.key(block).toString();
        this.fourWayBlock(block, this.models().fencePost(baseName + "_post", texture), this.models().fenceSide(baseName + "_side", texture));
        this.models().fenceInventory(baseName + "_inventory", texture);
    }

    public void buttonBlock(ButtonBlock block, ResourceLocation texture) {
        ModelFile button = this.models().button(this.name(block), texture);
        ModelFile buttonPressed = this.models().buttonPressed(this.name(block) + "_pressed", texture);
        this.buttonBlock(block, button, buttonPressed);
        this.models().buttonInventory(this.name(block) + "_inventory", texture);
    }

    protected String name(Block block) {
        return this.key(block).getPath();
    }

    protected ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}
