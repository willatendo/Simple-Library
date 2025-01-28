package willatendo.simplelibrary.server.util;

public enum Platform {
    FORGE(false),
    NEOFORGE(false),
    FABRIC(true);

    private final boolean isFabricLike;

    Platform(boolean isFabricLike) {
        this.isFabricLike = isFabricLike;
    }

    public boolean isFabricLike() {
        return this.isFabricLike;
    }
}
