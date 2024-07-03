package willatendo.simplelibrary.enumextender;

import java.util.List;

public interface EnumExtenderInitializer {
    default List<String> getRecipeBookTypes() {
        return List.of();
    }

    default List<ExtendedRecipeBookCategory> getRecipeBookCategories() {
        return List.of();
    }
}
