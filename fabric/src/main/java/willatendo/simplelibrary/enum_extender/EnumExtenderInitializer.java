package willatendo.simplelibrary.enum_extender;

import java.util.List;

public interface EnumExtenderInitializer {
    default List<String> getRecipeBookTypes() {
        return List.of();
    }
}
