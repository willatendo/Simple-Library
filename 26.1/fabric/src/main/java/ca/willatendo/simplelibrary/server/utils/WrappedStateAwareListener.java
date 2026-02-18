package ca.willatendo.simplelibrary.server.utils;

import net.fabricmc.fabric.impl.resource.FabricResourceReloader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class WrappedStateAwareListener implements PreparableReloadListener, FabricResourceReloader {
    private final Identifier identifier;
    private final PreparableReloadListener wrapped;

    public WrappedStateAwareListener(Identifier identifier, PreparableReloadListener wrapped) {
        this.identifier = identifier;
        this.wrapped = wrapped;
    }

    @Override
    public Identifier fabric$getId() {
        return this.identifier;
    }

    @Override
    public CompletableFuture<Void> reload(SharedState sharedState, Executor exectutor, PreparationBarrier barrier, Executor applyExectutor) {
        return this.wrapped.reload(sharedState, exectutor, barrier, applyExectutor);
    }
}
