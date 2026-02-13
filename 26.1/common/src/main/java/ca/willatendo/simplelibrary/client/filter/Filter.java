package ca.willatendo.simplelibrary.client.filter;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Filter {
    private final TagKey<Item> filterTag;
    private final ItemStack filterIcon;
    private List<Item> items;
    private boolean enabled = true;
    private FilterButton filterButton;

    public Filter(TagKey<Item> filterTag, ItemStack filterIcon) {
        this.filterTag = filterTag;
        this.filterIcon = filterIcon;
    }

    public TagKey<Item> getFilterTag() {
        return this.filterTag;
    }

    public ItemStack getFilterIcon() {
        return this.filterIcon;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Optional<List<Item>> getItems() {
        return Optional.ofNullable(this.items);
    }

    public void setFilterButton(FilterButton filterButton) {
        this.filterButton = filterButton;
    }

    public FilterButton getFilterButton() {
        return this.filterButton;
    }

    public void setVisible(boolean visible) {
        if (this.filterButton != null) {
            this.filterButton.visible = visible;
        }
    }

    public void setY(int y) {
        if (this.filterButton != null) {
            this.filterButton.setY(y);
        }
    }

    public void loadItems() {
        if (this.items != null) return;
        this.items = new ArrayList<>();
        BuiltInRegistries.ITEM.stream().forEach(item -> {
            if (item.builtInRegistryHolder().is(this.getFilterTag())) {
                this.items.add(item);
            }
        });
    }

    public void resetItems() {
        this.items = null;
    }
}
