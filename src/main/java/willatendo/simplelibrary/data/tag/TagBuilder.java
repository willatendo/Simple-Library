package willatendo.simplelibrary.data.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;

public class TagBuilder {
	private final List<TagEntry> removeEntries = new ArrayList<>();
	private final List<TagEntry> entries = new ArrayList<>();
	private boolean replace = false;

	public Stream<TagEntry> getRemoveEntries() {
		return this.removeEntries.stream();
	}

	public TagBuilder remove(TagEntry tagEntry) {
		this.removeEntries.add(tagEntry);
		return this;
	}

	public static TagBuilder create() {
		return new TagBuilder();
	}

	public List<TagEntry> build() {
		return List.copyOf(this.entries);
	}

	public TagBuilder add(TagEntry tagEntry) {
		this.entries.add(tagEntry);
		return this;
	}

	public TagBuilder addElement(ResourceLocation resourceLocation) {
		return this.add(TagEntry.element(resourceLocation));
	}

	public TagBuilder addOptionalElement(ResourceLocation resourceLocation) {
		return this.add(TagEntry.optionalElement(resourceLocation));
	}

	public TagBuilder addTag(ResourceLocation resourceLocation) {
		return this.add(TagEntry.tag(resourceLocation));
	}

	public TagBuilder addOptionalTag(ResourceLocation resourceLocation) {
		return this.add(TagEntry.optionalTag(resourceLocation));
	}

	public TagBuilder replace(boolean value) {
		this.replace = value;
		return this;
	}

	public TagBuilder replace() {
		return replace(true);
	}

	public boolean isReplace() {
		return this.replace;
	}
}
