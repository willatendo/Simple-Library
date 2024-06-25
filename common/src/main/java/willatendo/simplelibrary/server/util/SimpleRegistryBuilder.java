package willatendo.simplelibrary.server.util;

public final class SimpleRegistryBuilder {
    private boolean synced;

    private SimpleRegistryBuilder() {
    }

    public SimpleRegistryBuilder sync() {
        this.synced = true;
        return this;
    }

    public boolean isSynced() {
        return this.synced;
    }

    public static SimpleRegistryBuilder of() {
        return new SimpleRegistryBuilder();
    }
}
