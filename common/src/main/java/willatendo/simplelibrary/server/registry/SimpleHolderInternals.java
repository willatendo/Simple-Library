package willatendo.simplelibrary.server.registry;

import java.util.function.Supplier;

import net.minecraft.core.Holder;

// Internal Use
public abstract class SimpleHolderInternals<R, Y extends R> implements Holder<R>, Supplier<Y> {
}
