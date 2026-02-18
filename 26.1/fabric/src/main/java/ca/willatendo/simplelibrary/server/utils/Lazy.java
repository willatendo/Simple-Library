package ca.willatendo.simplelibrary.server.utils;

import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T> {
    private final Supplier<T> delegate;
    private volatile T cachedValue;

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    public synchronized void invalidate() {
        this.cachedValue = null;
    }

    private Lazy(Supplier<T> delegate) {
        this.delegate = delegate;
    }

    public T get() {
        T ret = this.cachedValue;
        if (ret == null) {
            synchronized (this) {
                ret = this.cachedValue;
                if (ret == null) {
                    this.cachedValue = ret = this.delegate.get();
                    if (ret == null) {
                        throw new IllegalStateException("Lazy value cannot be null, but supplier returned null: " + this.delegate);
                    }
                }
            }
        }

        return ret;
    }
}
