package willatendo.simplelibrary.common.config;

public enum ConfigType {
    CLIENT("client"),
    COMMON("common");

    private final String name;

    ConfigType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
