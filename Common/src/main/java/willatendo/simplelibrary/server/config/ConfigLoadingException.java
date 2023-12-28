package willatendo.simplelibrary.server.config;

public class ConfigLoadingException extends RuntimeException {
	public ConfigLoadingException() {
		super("Config Failed to Load Value!");
	}
}
