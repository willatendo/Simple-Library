package willatendo.simplelibrary;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import willatendo.simplelibrary.enumextender.EnumExtenderInitializer;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumExtender implements Runnable {
    @Override
    public void run() {
        switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> loadClient();
            case SERVER -> loadServer();
        }
    }

    private static void loadClient() {
        MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
        String recipeBookType = mappingResolver.mapClassName("intermediary", "net.minecraft.class_5421");
        String recipeBookCategories = mappingResolver.mapClassName("intermediary", "net.minecraft.class_314");
        String itemStackArray = "[L" + mappingResolver.mapClassName("intermediary", "net.minecraft.class_1799") + ";";

        EnumAdder recipeBookTypeAdder = ClassTinkerers.enumBuilder(recipeBookType);
        EnumAdder recipeBookCategoriesAdder = ClassTinkerers.enumBuilder(recipeBookCategories, itemStackArray);

        List<EnumExtenderInitializer> enumExtenderInitializers = FabricLoader.getInstance().getEntrypoints(SimpleUtils.SIMPLE_ID + ":enum_initializer", EnumExtenderInitializer.class);
        enumExtenderInitializers.forEach(enumExtenderInitializer -> enumExtenderInitializer.getRecipeBookTypes().forEach(name -> {
            recipeBookTypeAdder.addEnum(name.toUpperCase());
        }));
        enumExtenderInitializers.forEach(enumExtenderInitializer -> enumExtenderInitializer.getRecipeBookCategories().forEach(extendedRecipeBookCategory -> {
            recipeBookCategoriesAdder.addEnum(extendedRecipeBookCategory.name().toUpperCase(), () -> new Object[]{Arrays.stream(extendedRecipeBookCategory.items()).map(itemKey -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemKey))).map(ItemStack::new).toArray(ItemStack[]::new)});
        }));

        recipeBookTypeAdder.build();
        recipeBookCategoriesAdder.build();
    }

    private static void loadServer() {
        MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
        String recipeBookType = mappingResolver.mapClassName("intermediary", "net.minecraft.class_5421");
        EnumAdder recipeBookTypeAdder = ClassTinkerers.enumBuilder(recipeBookType);

        List<EnumExtenderInitializer> enumExtenderInitializers = FabricLoader.getInstance().getEntrypoints(SimpleUtils.SIMPLE_ID + ":enum_initializer", EnumExtenderInitializer.class);
        enumExtenderInitializers.forEach(enumExtenderInitializer -> enumExtenderInitializer.getRecipeBookTypes().forEach(name -> {
            recipeBookTypeAdder.addEnum(name);
        }));

        recipeBookTypeAdder.build();
    }
}
