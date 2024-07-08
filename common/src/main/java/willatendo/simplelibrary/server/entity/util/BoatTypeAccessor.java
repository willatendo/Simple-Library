package willatendo.simplelibrary.server.entity.util;

import net.minecraft.core.Holder;
import willatendo.simplelibrary.server.entity.variant.BoatType;

public interface BoatTypeAccessor {
    Holder<BoatType> getBoatType();

    void setBoatType(Holder<BoatType> boatType);
}
