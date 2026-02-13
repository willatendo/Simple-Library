package ca.willatendo.simplelibrary.server.biome_modifier;

import net.minecraft.world.level.biome.Biome;

// Modified from Neoforge
public final class ClimateSettingsBuilder {
    private boolean hasPrecipitation;
    private float temperature;
    private Biome.TemperatureModifier temperatureModifier;
    private float downfall;

    public static ClimateSettingsBuilder copyOf(Biome.ClimateSettings settings) {
        return ClimateSettingsBuilder.create(settings.hasPrecipitation(), settings.temperature(), settings.temperatureModifier(), settings.downfall());
    }

    public static ClimateSettingsBuilder create(boolean hasPrecipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall) {
        return new ClimateSettingsBuilder(hasPrecipitation, temperature, temperatureModifier, downfall);
    }

    private ClimateSettingsBuilder(boolean hasPrecipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall) {
        this.hasPrecipitation = hasPrecipitation;
        this.temperature = temperature;
        this.temperatureModifier = temperatureModifier;
        this.downfall = downfall;
    }

    public Biome.ClimateSettings build() {
        return new Biome.ClimateSettings(this.hasPrecipitation, this.temperature, this.temperatureModifier, this.downfall);
    }

    public boolean hasPrecipitation() {
        return hasPrecipitation;
    }

    public void setHasPrecipitation(boolean hasPrecipitation) {
        this.hasPrecipitation = hasPrecipitation;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public Biome.TemperatureModifier getTemperatureModifier() {
        return temperatureModifier;
    }

    public void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
        this.temperatureModifier = temperatureModifier;
    }

    public float getDownfall() {
        return downfall;
    }

    public void setDownfall(float downfall) {
        this.downfall = downfall;
    }
}
