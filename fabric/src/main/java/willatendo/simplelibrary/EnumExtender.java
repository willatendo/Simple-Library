package willatendo.simplelibrary;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import willatendo.simplelibrary.enum_extender.EnumExtenderInitializer;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.util.List;

public class EnumExtender implements Runnable {
    @Override
    public void run() {
        switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> EnumExtender.loadClient();
            case SERVER -> EnumExtender.loadServer();
        }
    }

    private static void loadClient() {
        MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
        String recipeBookType = mappingResolver.mapClassName("intermediary", "net.minecraft.class_5421");

        EnumAdder recipeBookTypeAdder = ClassTinkerers.enumBuilder(recipeBookType);

        List<EnumExtenderInitializer> enumExtenderInitializers = FabricLoader.getInstance().getEntrypoints(SimpleUtils.SIMPLE_ID + ":enum_initializer", EnumExtenderInitializer.class);
        enumExtenderInitializers.forEach(enumExtenderInitializer -> {
            enumExtenderInitializer.getRecipeBookTypes().forEach(name -> recipeBookTypeAdder.addEnum(name.toUpperCase()));
        });

        recipeBookTypeAdder.build();
    }

    private static void loadServer() {
        MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
        String recipeBookType = mappingResolver.mapClassName("intermediary", "net.minecraft.class_5421");
        EnumAdder recipeBookTypeAdder = ClassTinkerers.enumBuilder(recipeBookType);

        List<EnumExtenderInitializer> enumExtenderInitializers = FabricLoader.getInstance().getEntrypoints(SimpleUtils.SIMPLE_ID + ":enum_initializer", EnumExtenderInitializer.class);
        enumExtenderInitializers.forEach(enumExtenderInitializer -> enumExtenderInitializer.getRecipeBookTypes().forEach(name -> recipeBookTypeAdder.addEnum(name.toUpperCase())));

        recipeBookTypeAdder.build();
    }
}
