package willatendo.simplelibrary.server.util;

import java.util.function.Supplier;

public interface UniversalRegister<T> {
	Supplier<T> register(String id, Supplier<T> object);
}
