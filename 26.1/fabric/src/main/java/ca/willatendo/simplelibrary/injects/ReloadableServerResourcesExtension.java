package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.conditions.ICondition;

public interface ReloadableServerResourcesExtension {
    default ICondition.IContext getConditionContext() {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
