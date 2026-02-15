package ca.willatendo.simplelibrary.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public abstract class SimpleSoundDefinitionsProvider extends SoundDefinitionsProvider {
    private final String modId;

    public SimpleSoundDefinitionsProvider(PackOutput output, String modId) {
        super(output, modId);
        this.modId = modId;
    }

    public void add(SoundEvent soundEvent, String base, String type, String event, String... sounds) {
        SoundDefinition soundDefinition = SoundDefinition.definition().subtitle("subtitles." + type + "." + base + "." + event);
        for (String sound : sounds) {
            soundDefinition.with(SoundDefinition.Sound.sound(Identifier.fromNamespaceAndPath(this.modId, sound), SoundDefinition.SoundType.SOUND));
        }
        this.add(soundEvent, soundDefinition);
    }

    public void block(SoundEvent soundEvent, String base, String event, String... sounds) {
        this.add(soundEvent, base, "block", event, sounds);
    }

    public void entity(SoundEvent soundEvent, String base, String event, String... sounds) {
        this.add(soundEvent, base, "entity", event, sounds);
    }

    public void item(SoundEvent soundEvent, String base, String event, String... sounds) {
        this.add(soundEvent, base, "item", event, sounds);
    }

    @Override
    public String getName() {
        return this.modId + ": Sound Defs";
    }
}
