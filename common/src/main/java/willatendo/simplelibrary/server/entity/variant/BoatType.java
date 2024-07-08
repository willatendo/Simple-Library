package willatendo.simplelibrary.server.entity.variant;

import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import willatendo.simplelibrary.server.item.SimpleBoatItem;

public final record BoatType(String name, boolean raft, Holder<SimpleBoatItem> boat, Holder<SimpleBoatItem> chestBoat, Holder<Block> block) implements StringRepresentable {
    public BoatType(String name, Holder<SimpleBoatItem> boat, Holder<SimpleBoatItem> chestBoat, Holder<Block> block) {
        this(name, false, boat, chestBoat, block);
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
