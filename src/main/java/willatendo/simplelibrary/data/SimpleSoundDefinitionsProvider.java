package willatendo.simplelibrary.data;

import java.util.Optional;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinition.Sound;
import net.minecraftforge.common.data.SoundDefinition.SoundType;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import willatendo.simplelibrary.server.util.SimpleUtils;

public abstract class SimpleSoundDefinitionsProvider extends SoundDefinitionsProvider {
	public SimpleSoundDefinitionsProvider(PackOutput packOutput, String modid, ExistingFileHelper existingFileHelper) {
		super(packOutput, modid, existingFileHelper);
	}

	public void addAll(DeferredRegister<SoundEvent> soundEvents, RegistryObject<? extends SoundEvent>... exceptions) {
		for (RegistryObject<SoundEvent> soundEvent : soundEvents.getEntries().stream().filter(s -> !SimpleUtils.toList(exceptions).contains(s)).toList()) {
			this.add(soundEvent, this.basicSound(soundEvent));
		}
	}

	public void add(RegistryObject<SoundEvent> soundEvent, Sound... sounds) {
		this.add(soundEvent, Optional.empty(), sounds);
	}

	public void add(RegistryObject<SoundEvent> soundEvent, Optional<String> subtitle, Sound... sounds) {
		SoundDefinition soundDefinition = SoundDefinition.definition();
		for (Sound sound : sounds) {
			soundDefinition.with(sound);
		}
		if (!subtitle.isEmpty()) {
			soundDefinition.subtitle(subtitle.get());
		}
	}

	public Sound basicSound(RegistryObject<SoundEvent> soundEvent) {
		return Sound.sound(this.getSoundFile(soundEvent), SoundType.SOUND);
	}

	public Sound basicSoundEvent(RegistryObject<SoundEvent> soundEvent) {
		return Sound.sound(this.getSoundFile(soundEvent), SoundType.EVENT);
	}

	public ResourceLocation getSoundFile(RegistryObject<SoundEvent> soundEvent) {
		return new ResourceLocation(soundEvent.getId().getNamespace(), soundEvent.getId().getPath());
	}
}
