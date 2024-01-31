package willatendo.simplelibrary.data.model;

import com.google.common.base.Preconditions;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.resources.ResourceLocation;

public abstract class ModelFile {
	protected ResourceLocation resourceLocation;

	protected ModelFile(ResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	protected abstract boolean exists();

	public ResourceLocation getResourceLocation() {
		this.assertExistence();
		return this.resourceLocation;
	}

	public void assertExistence() {
		Preconditions.checkState(exists(), "Model at %s does not exist", resourceLocation);
	}

	public ResourceLocation getUncheckedLocation() {
		return this.resourceLocation;
	}

	public static class UncheckedModelFile extends ModelFile {
		public UncheckedModelFile(String path) {
			this(new ResourceLocation(path));
		}

		public UncheckedModelFile(ResourceLocation resourceLocation) {
			super(resourceLocation);
		}

		@Override
		protected boolean exists() {
			return true;
		}
	}

	public static class ExistingModelFile extends ModelFile {
		private final ExistingFileHelper existingFileHelper;

		public ExistingModelFile(ResourceLocation resourceLocation, ExistingFileHelper existingFileHelper) {
			super(resourceLocation);
			this.existingFileHelper = existingFileHelper;
		}

		@Override
		protected boolean exists() {
			if (getUncheckedLocation().getPath().contains(".")) {
				return existingFileHelper.exists(getUncheckedLocation(), ModelProvider.MODEL_WITH_EXTENSION);
			} else {
				return existingFileHelper.exists(getUncheckedLocation(), ModelProvider.MODEL);
			}
		}
	}
}
