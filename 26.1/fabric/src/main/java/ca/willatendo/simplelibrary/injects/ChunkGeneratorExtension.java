package ca.willatendo.simplelibrary.injects;

public interface ChunkGeneratorExtension {
    default void refreshFeaturesPerStep() {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
