package willatendo.simplelibrary.client.filter;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public final class CreativeModeTabFilter {
    public static final List<CreativeModeTabFilter> CREATIVE_MODE_TAB_FILTERS = Lists.newArrayList();
    public static final List<CreativeModeTab> FILTERED_CREATIVE_MODE_TABS = Lists.newArrayList();
    private final CreativeModeTab creativeModeTab;
    private final List<Filter> categories;
    private AbstractWidget scrollUpButton;
    private AbstractWidget scrollDownButton;
    private CreativeModeTab lastTab;
    private int guiLeft;
    private int guiTop;
    private int scroll;

    public static void create(CreativeModeTab creativeModeTab, ImmutableList.Builder<Filter> categories) {
        FILTERED_CREATIVE_MODE_TABS.add(creativeModeTab);
        CreativeModeTabFilter creativeModeTabFilter = new CreativeModeTabFilter(creativeModeTab, categories.build());
        CREATIVE_MODE_TAB_FILTERS.add(creativeModeTabFilter);
    }

    private CreativeModeTabFilter(CreativeModeTab creativeModeTab, List<Filter> categories) {
        this.creativeModeTab = creativeModeTab;
        this.categories = categories;
    }

    public void modifyWidgetsEvent(Screen screen, Consumer<AbstractWidget> add) {
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            this.guiLeft = creativeScreen.leftPos;
            this.guiTop = creativeScreen.topPos;
            this.categories.forEach(Filter::loadItems);
            this.injectWidgets(creativeScreen, add);
        }
    }

    public void closedEvent(Screen screen) {
        if (screen instanceof CreativeModeInventoryScreen) {
            this.categories.forEach(category -> {
                this.scrollUpButton = null;
                this.scrollDownButton = null;
                category.setFilterButton(null);
            });
        }
    }

    public void afterDrawEvent(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            CreativeModeTab selectedTab = CreativeModeInventoryScreen.selectedTab;
            if (this.lastTab != selectedTab) {
                this.onSwitchCreativeTab(selectedTab, creativeScreen);
                this.lastTab = selectedTab;
            }
            this.drawFilterTabTooltips(guiGraphics, mouseX, mouseY);
        }
    }

    public void loggingOutEvent() {
        this.categories.forEach(category -> {
            category.resetItems();
            category.setEnabled(true);
        });
    }

    private void injectWidgets(CreativeModeInventoryScreen screen, Consumer<AbstractWidget> add) {
        this.categories.forEach(category -> {
            FilterButton filterButton = new FilterButton(this.guiLeft - 28, this.guiTop, category, btn -> {
                if (Screen.hasControlDown() || Screen.hasShiftDown()) {
                    category.setEnabled(!category.isEnabled());
                } else {
                    this.categories.forEach(c -> c.setEnabled(false));
                    category.setEnabled(true);
                }
                this.updateItems(screen);
            });
            filterButton.visible = false;
            add.accept(filterButton);
        });
        add.accept(this.scrollUpButton = new ArrowButton(this.guiLeft - 22, this.guiTop - 12, true, btn -> {
            if (this.scroll > 0) this.scroll--;
            this.updateWidgets();
        }));
        add.accept(this.scrollDownButton = new ArrowButton(this.guiLeft - 22, this.guiTop + 127, false, btn -> {
            if (this.scroll <= this.categories.size() - 4 - 1) this.scroll++;
            this.updateWidgets();
        }));
        this.updateWidgets();
        this.onSwitchCreativeTab(CreativeModeInventoryScreen.selectedTab, screen);
    }

    private void updateItems(CreativeModeInventoryScreen screen) {
        Set<Item> seenItems = new HashSet<>();
        LinkedHashSet<ItemStack> sortedItemStacks = new LinkedHashSet<>();
        this.creativeModeTab.getDisplayItems().forEach(itemStack -> this.categories.stream().filter(Filter::isEnabled).forEach(category -> {
            Item item = itemStack.getItem();
            if (!seenItems.contains(item) && itemStack.is(category.getFilterTag())) {
                sortedItemStacks.add(itemStack.copy());
                seenItems.add(item);
            }
        }));
        NonNullList<ItemStack> items = screen.getMenu().items;
        items.clear();
        items.addAll(sortedItemStacks);
        screen.getMenu().scrollTo(0);
    }

    private void updateWidgets() {
        this.categories.forEach(category -> category.setVisible(false));
        for (int scroll = this.scroll; scroll < this.scroll + 4 && scroll < this.categories.size(); scroll++) {
            Filter filter = this.categories.get(scroll);
            filter.setY(this.guiTop + 29 * (scroll - this.scroll) + 11);
            filter.setVisible(true);
        }
        this.scrollUpButton.active = this.scroll > 0;
        this.scrollDownButton.active = this.scroll <= this.categories.size() - 4 - 1;
    }

    private void onSwitchCreativeTab(CreativeModeTab creativeModeTab, CreativeModeInventoryScreen screen) {
        boolean update = FILTERED_CREATIVE_MODE_TABS.contains(creativeModeTab);
        this.scrollUpButton.visible = update;
        this.scrollDownButton.visible = update;
        if (update) {
            this.updateWidgets();
            this.updateItems(screen);
            return;
        }
        this.categories.forEach(category -> category.setVisible(false));
    }

    public boolean onMouseScroll(double mouseX, double mouseY, double scroll) {
        CreativeModeTab selectedTab = CreativeModeInventoryScreen.selectedTab;
        if (!FILTERED_CREATIVE_MODE_TABS.contains(selectedTab)) {
            return false;
        }

        double startX = this.guiLeft - 28;
        double startY = this.guiTop + 29;
        if (mouseX >= startX && mouseX < startX + 28 && mouseY >= startY && mouseY < startY + 113) {
            int oldScroll = this.scroll;
            this.scroll += scroll > 0 ? -1 : 1;
            this.scroll = Mth.clamp(this.scroll, 0, this.categories.size() - 4);
            if (this.scroll != oldScroll) {
                this.updateWidgets();
            }
            return true;
        }
        return false;
    }

    private void drawFilterTabTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (Filter category : this.categories) {
            FilterButton filterButton = category.getFilterButton();
            if (filterButton != null && filterButton.visible && filterButton.isHovered()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, filterButton.getFilterTooltip(), mouseX, mouseY);
                return;
            }
        }
    }
}
