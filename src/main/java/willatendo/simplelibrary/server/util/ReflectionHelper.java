package willatendo.simplelibrary.server.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

public final class ReflectionHelper {
	private static final Map<String, Field> FIELDS_CACHE = Maps.newIdentityHashMap();
	private static final Map<String, Method> METHODS_CACHE = Maps.newIdentityHashMap();
	private static final Map<String, Constructor<?>> CONSTRUCTORS_CACHE = Maps.newIdentityHashMap();

	@Nullable
	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, true);
	}

	@Nullable
	public static Field findField(Class<?> clazz, String name, boolean allowCache) {
		return findField(clazz.getTypeName(), name, allowCache);
	}

	@Nullable
	public static Field findField(String typeName, String name, boolean allowCache) {
		Objects.requireNonNull(typeName, "clazz name was null");
		Objects.requireNonNull(name, "field name was null");
		String fieldName = getClassMemberName(typeName, name);
		if (allowCache && FIELDS_CACHE.containsKey(fieldName)) {
			return FIELDS_CACHE.get(fieldName);
		}
		try {
			Field field = Class.forName(typeName).getDeclaredField(name);
			field.setAccessible(true);
			FIELDS_CACHE.put(fieldName, field);
			return field;
		} catch (NoSuchFieldException | ClassNotFoundException e) {
			SimpleUtils.LOGGER.warn("Unable to find field {}", fieldName, e);
		}
		FIELDS_CACHE.put(fieldName, null);
		return null;
	}

	@Nullable
	public static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		return findMethod(clazz, name, true, parameterTypes);
	}

	@Nullable
	public static Method findMethod(Class<?> clazz, String name, boolean allowCache, Class<?>... parameterTypes) {
		return findMethod(clazz.getTypeName(), name, allowCache, parameterTypes);
	}

	@Nullable
	public static Method findMethod(String typeName, String name, boolean allowCache, Class<?>... parameterTypes) {
		Objects.requireNonNull(typeName, "clazz name was null");
		Objects.requireNonNull(name, "method name was null");
		String methodName = getMethodName(typeName, name, parameterTypes);
		if (allowCache && METHODS_CACHE.containsKey(methodName)) {
			return METHODS_CACHE.get(methodName);
		}
		try {
			Method method = Class.forName(typeName).getDeclaredMethod(name, parameterTypes);
			method.setAccessible(true);
			METHODS_CACHE.put(methodName, method);
			return method;
		} catch (NoSuchMethodException | ClassNotFoundException e) {
			SimpleUtils.LOGGER.warn("Unable to find method {}", methodName, e);
		}
		METHODS_CACHE.put(methodName, null);
		return null;
	}

	@Nullable
	public static <T> Constructor<T> findConstructor(Class<?> clazz, Class<?>... parameterTypes) {
		return findConstructor(clazz, true, parameterTypes);
	}

	@Nullable
	public static <T> Constructor<T> findConstructor(Class<?> clazz, boolean allowCache, Class<?>... parameterTypes) {
		return findConstructor(clazz.getTypeName(), allowCache, parameterTypes);
	}

	@Nullable
	public static <T> Constructor<T> findConstructor(String typeName, boolean allowCache, Class<?>... parameterTypes) {
		Objects.requireNonNull(typeName, "clazz name was null");
		String constructorName = getConstructorName(typeName, parameterTypes);
		if (allowCache && CONSTRUCTORS_CACHE.containsKey(constructorName)) {
			return (Constructor<T>) CONSTRUCTORS_CACHE.get(constructorName);
		}
		try {
			Constructor<T> constructor = (Constructor<T>) Class.forName(typeName).getDeclaredConstructor(parameterTypes);
			constructor.setAccessible(true);
			CONSTRUCTORS_CACHE.put(constructorName, constructor);
			return constructor;
		} catch (NoSuchMethodException | ClassNotFoundException e) {
			SimpleUtils.LOGGER.warn("Unable to find constructor {}", constructorName, e);
		}
		CONSTRUCTORS_CACHE.put(constructorName, null);
		return null;
	}

	public static <T, E> Optional<T> getValue(Class<? super E> clazz, String name, E instance) {
		return getValue(findField(clazz, name), instance);
	}

	public static <T, E> Optional<T> getValue(String typeName, String name, E instance) {
		return getValue(findField(typeName, name, true), instance);
	}

	public static <T, E> boolean setValue(Class<? super E> clazz, String name, E instance, T value) {
		return setValue(findField(clazz, name), instance, value);
	}

	public static <T, E> boolean setValue(String typeName, String name, E instance, T value) {
		return setValue(findField(typeName, name, true), instance, value);
	}

	public static <T> Optional<T> getValue(@Nullable Field field, Object instance) {
		if (field != null) {
			try {
				return Optional.ofNullable((T) field.get(instance));
			} catch (IllegalAccessException e) {
				SimpleUtils.LOGGER.warn("Unable to access field {}", getFieldName(field), e);
			}
		}
		return Optional.empty();
	}

	public static <T> boolean setValue(@Nullable Field field, Object instance, T value) {
		if (field != null) {
			try {
				field.set(instance, value);
				return true;
			} catch (IllegalAccessException e) {
				SimpleUtils.LOGGER.warn("Unable to access field {}", getFieldName(field), e);
			}
		}
		return false;
	}

	public static <T, E> Optional<T> invokeMethod(Class<? super E> clazz, String name, Class<?>[] parameterTypes, E instance, Object[] args) {
		return invokeMethod(findMethod(clazz, name, parameterTypes), instance, args);
	}

	public static <T> Optional<T> invokeMethod(@Nullable Method method, Object instance, Object... args) {
		if (method != null) {
			try {
				return Optional.ofNullable((T) method.invoke(instance, args));
			} catch (InvocationTargetException | IllegalAccessException e) {
				SimpleUtils.LOGGER.warn("Unable to access method {}", getMethodName(method), e);
			}
		}
		return Optional.empty();
	}

	public static <T, E> Optional<T> newInstance(Class<? super E> clazz, Class<?>[] parameterTypes, Object[] args) {
		return newInstance(findConstructor(clazz, parameterTypes), args);
	}

	public static <T> Optional<T> newInstance(@Nullable Constructor<T> constructor, Object... args) {
		if (constructor != null) {
			try {
				return Optional.of(constructor.newInstance(args));
			} catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
				SimpleUtils.LOGGER.warn("Unable to access constructor {}", getConstructorName(constructor), e);
			}
		}
		return Optional.empty();
	}

	private static String getFieldName(@NotNull Field field) {
		Objects.requireNonNull(field, "Cannot get name for null field");
		return getClassMemberName(field.getDeclaringClass(), field.getName());
	}

	private static String getMethodName(@NotNull Method method) {
		Objects.requireNonNull(method, "Cannot get name for null method");
		return getMethodName(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
	}

	private static String getConstructorName(@NotNull Constructor<?> constructor) {
		Objects.requireNonNull(constructor, "Cannot get name for null constructor");
		return getConstructorName(constructor.getDeclaringClass(), constructor.getParameterTypes());
	}

	private static String getConstructorName(Class<?> clazz, Class<?>... parameterTypes) {
		return getConstructorName(clazz.getTypeName(), parameterTypes);
	}

	private static String getConstructorName(String typeName, Class<?>... parameterTypes) {
		return getMethodName(typeName, "<init>", parameterTypes);
	}

	private static String getMethodName(Class<?> clazz, String method, Class<?>... parameterTypes) {
		return getMethodName(clazz.getTypeName(), method, parameterTypes);
	}

	private static String getMethodName(String typeName, String method, Class<?>... parameterTypes) {
		return getClassMemberName(typeName, toMethodSignature(method, parameterTypes));
	}

	private static String toMethodSignature(String method, Class<?>... parameterTypes) {
		StringJoiner stringJoiner = new StringJoiner(",", method + "(", ")");
		for (Class<?> parameterType : parameterTypes) {
			stringJoiner.add(parameterType.getTypeName());
		}
		return stringJoiner.toString();
	}

	private static String getClassMemberName(Class<?> clazz, String member) {
		return getClassMemberName(clazz.getTypeName(), member);
	}

	private static String getClassMemberName(String typeName, String member) {
		return (typeName + "." + member).intern();
	}
}
