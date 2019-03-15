package org.jiuwo.fastel.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jiuwo.fastel.constant.SysConstant;
import org.jiuwo.fastel.exception.FastElException;

/**
 * @author Steven Han
 */
public class ReflectUtil {

    private static final Map<Class<?>, Map<String, Method>> READER_MAP = newMap();
    private static final Map<Class<?>, Map<String, Method>> WRITER_MAP = newMap();
    private static final Map<Class<?>, Map<String, Type[]>> TYPE_MAP = newMap();
    private static final Map<Class<?>, Map<String, Field>> FIELD_MAP = newMap();
    private static final Map<Class<?>, Constructor<?>> CONSTRUCTOR_MAP = newMap();
    private static final Map<Class<? extends Enum<?>>, Enum<?>[]> ENUM_MAP = newMap();
    private static Object initLock = new Object();

    private static <T> T newMap() {
        return (T) new HashMap();
    }

    public static Map<String, Method> getGetterMap(final Class<?> clazz) {
        Map<String, Method> propertyMap = READER_MAP.get(clazz);
        if (propertyMap == null) {
            initProperties(clazz);
            propertyMap = READER_MAP.get(clazz);
        }
        return propertyMap;
    }

    public static Map<String, Method> getSetterMap(final Class<?> clazz) {
        Map<String, Method> propertyMap = WRITER_MAP.get(clazz);
        if (propertyMap == null) {
            initProperties(clazz);
            propertyMap = WRITER_MAP.get(clazz);
        }
        return propertyMap;
    }

    private static Map<String, Type[]> getTypeMap(final Class<?> clazz) {
        Map<String, Type[]> tMap = TYPE_MAP.get(clazz);
        if (tMap == null) {
            initProperties(clazz);
            tMap = TYPE_MAP.get(clazz);
        }
        return tMap;
    }

    public static Map<String, Field> getFieldMap(final Class<?> clazz) {
        Map<String, Field> fMap = FIELD_MAP.get(clazz);
        if (fMap == null) {
            initProperties(clazz);
            fMap = FIELD_MAP.get(clazz);
        }
        return fMap;
    }

    private static void initProperties(final Class<?> clazz) {
        synchronized (initLock) {
            try {
                Constructor<?> cons = clazz.getDeclaredConstructor();
                if (!cons.isAccessible()) {
                    cons.setAccessible(true);
                }
                CONSTRUCTOR_MAP.put(clazz, cons);
            } catch (Exception e) {
            }
            HashMap<String, Method> getterMap = new HashMap<String, Method>();
            HashMap<String, Method> setterMap = new HashMap<String, Method>();
            HashMap<String, Type[]> propertyMap = new HashMap<String, Type[]>();
            HashMap<String, Field> fieldMap = new HashMap<String, Field>();
            try {
                if (!clazz.equals(Object.class)) {
                    getterMap.putAll(getGetterMap(clazz.getSuperclass()));
                    setterMap.putAll(getSetterMap(clazz.getSuperclass()));
                    propertyMap.putAll(getTypeMap(clazz.getSuperclass()));
                    fieldMap.putAll(getFieldMap(clazz.getSuperclass()));
                }
                Method[] methods = clazz.getDeclaredMethods();

                for (Method m : methods) {
                    if ((m.getModifiers() & Modifier.PUBLIC) > 0) {
                        Class<? extends Object> type = m.getReturnType();
                        Class<? extends Object>[] params = m
                                .getParameterTypes();
                        String name = m.getName();
                        if (type == Void.TYPE) {
                            if (params.length == 1 && name.startsWith(SysConstant.SYS_SET)) {
                                type = params[0];
                                initMethod(setterMap, propertyMap, m,
                                        name.substring(3));
                            }
                        } else {
                            if (params.length == 0) {
                                if (name.startsWith(SysConstant.SYS_GET)
                                        && !SysConstant.SYS_GET_CLASS.equals(name)) {
                                    initMethod(getterMap, propertyMap, m,
                                            name.substring(3));
                                } else if (type == Boolean.TYPE
                                        && name.startsWith(SysConstant.SYS_IS)) {
                                    initMethod(getterMap, propertyMap, m,
                                            name.substring(2));
                                }
                            }
                        }
                    }
                }
                boolean nogs = propertyMap.isEmpty();
                boolean isMember = clazz.isMemberClass();

                Field[] fields = clazz.getDeclaredFields();
                for (Field f : fields) {
                    f.setAccessible(true);
                    String name = f.getName();
                    if (nogs
                            && (isMember || 0 < (f.getModifiers() & Modifier.PUBLIC))) {
                        propertyMap.put(name, new Type[]{f.getGenericType(),
                                clazz});
                    }
                    fieldMap.put(name, f);
                }
            } catch (Exception e) {
//                log.warn("初始化属性集合异常", e);
            } finally {
                READER_MAP.put(clazz, Collections.unmodifiableMap(getterMap));
                WRITER_MAP.put(clazz, Collections.unmodifiableMap(setterMap));
                TYPE_MAP.put(clazz, Collections.unmodifiableMap(propertyMap));
                FIELD_MAP.put(clazz, Collections.unmodifiableMap(fieldMap));
            }
        }
    }

    private static void initMethod(Map<String, Method> propertyMap,
                                   Map<String, Type[]> typeMap, Method m, String name) {
        if (name.length() > 0) {
            char c = name.charAt(0);
            if (Character.isUpperCase(c)) {
                name = Character.toLowerCase(c) + name.substring(1);
                m.setAccessible(true);
                propertyMap.put(name, m);
                Type type = m.getGenericReturnType();
                if (type == Void.TYPE) {
                    type = m.getParameterTypes()[0];
                }
                Type[] ot = typeMap.get(name);
                if (ot != null) {
                    if (ot[0] != type) {
//                        log.warn("属性类型冲突：" + ot + "!=" + type);
                    }
                }
                typeMap.put(name, new Type[]{type, m.getDeclaringClass()});
            }
        }

    }

    private static int toIndex(Object key) {
        return key instanceof Number ? ((Number) key).intValue() : Integer
                .parseInt(String.valueOf(key));
    }

    public static Class<? extends Object> getValueType(Type type) {
        Type result = null;
        Class<? extends Object> clazz = null;
        if (type instanceof ParameterizedType) {
            clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        } else {
            clazz = (Class<?>) type;
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            result = getParameterizedType(type, Collection.class, 0);
        } else if (Map.class.isAssignableFrom(clazz)) {
            result = getParameterizedType(type, Map.class, 1);
        }
        if (result != null) {
            return baseClass(result);
        }
        return Object.class;
    }


    public static Type getParameterizedType(final Type ownerType,
                                            final Class<?> declaredClass, final Type declaredType) {
        if (declaredType instanceof TypeVariable) {
            String name = ((TypeVariable<?>) declaredType).getName();
            TypeVariable<?>[] typeVariables = declaredClass.getTypeParameters();
            if (typeVariables != null) {
                for (int i = 0; i < typeVariables.length; i++) {
                    if (name.equals(typeVariables[i].getName())) {
                        return getParameterizedType(ownerType, declaredClass, i);
                    }
                }
            }
            return declaredType;
        } else if (declaredType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) declaredType;
            final Type[] types = parameterizedType.getActualTypeArguments();
            boolean changed = false;
            for (int i = 0; i < types.length; i++) {
                Type argumentType = types[i];
                Type trueType = getParameterizedType(ownerType, declaredClass, argumentType);
                if (argumentType != trueType) {
                    types[i] = trueType;
                    changed = true;
                }
            }
            if (changed) {
                return changedParameterizedType(parameterizedType, types);
            }
        }
        // class
        // parameterizedType
        return declaredType;

    }

    private static Type changedParameterizedType(final ParameterizedType parameterizedType,
                                                 final Type[] changedTypes) {
        return new ParameterizedType() {
            @Override
            public Type getRawType() {
                return parameterizedType.getRawType();
            }

            @Override
            public Type getOwnerType() {
                return parameterizedType.getOwnerType();
            }

            @Override
            public Type[] getActualTypeArguments() {
                return changedTypes;
            }
        };
    }

    public static Type getParameterizedType(final Type ownerType,
                                            final Class<?> declaredClass, int paramIndex) {
        Class<?> clazz = null;
        ParameterizedType pt = null;
        Type[] ats = null;
        TypeVariable<?>[] tps = null;
        if (ownerType instanceof ParameterizedType) {
            pt = (ParameterizedType) ownerType;
            clazz = (Class<?>) pt.getRawType();
            ats = pt.getActualTypeArguments();
            tps = clazz.getTypeParameters();
        } else {
            clazz = (Class<?>) ownerType;
        }
        if (declaredClass == clazz) {
            if (pt != null) {
                return pt.getActualTypeArguments()[paramIndex];
            }
            return Object.class;
        }
        Class<?>[] ifs = clazz.getInterfaces();
        for (int i = 0; i < ifs.length; i++) {
            Class<?> ifc = ifs[i];
            if (declaredClass.isAssignableFrom(ifc)) {
                return getTureType(
                        getParameterizedType(clazz.getGenericInterfaces()[i],
                                declaredClass, paramIndex), tps, ats);
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            if (declaredClass.isAssignableFrom(superClass)) {
                return getTureType(
                        getParameterizedType(clazz.getGenericSuperclass(),
                                declaredClass, paramIndex), tps, ats);
            }
        }
        throw new IllegalArgumentException("查找真实类型失败:" + ownerType);
    }

    private static Type getTureType(Type type, TypeVariable<?>[] typeVariables,
                                    Type[] actualTypes) {
        if (type instanceof TypeVariable<?>) {
            TypeVariable<?> tv = (TypeVariable<?>) type;
            String name = tv.getName();
            if (actualTypes != null) {
                for (int i = 0; i < typeVariables.length; i++) {
                    if (name.equals(typeVariables[i].getName())) {
                        return actualTypes[i];
                    }
                }
            }
            return tv;
            // }else if (type instanceof Class<?>) {
            // return type;
        }
        return type;
    }


    public static Class<? extends Object> baseClass(Type result) {
        if (result instanceof Class<?>) {
            return (Class<?>) result;
        } else if (result instanceof ParameterizedType) {
            return baseClass(((ParameterizedType) result).getRawType());
        } else if (result instanceof WildcardType) {
            return baseClass(((WildcardType) result).getUpperBounds()[0]);
        }
        return null;
    }

    public static Class<?> getPropertyClass(Type type, Object key) {
        return baseClass(getPropertyType(type, key));
    }

    public static Type getPropertyType(Type type, Object key) {
        Class<?> clazz = baseClass(type);
        if (clazz != null) {
            if (Collection.class.isAssignableFrom(clazz)) {
                return getValueType(type);
            } else if (Map.class.isAssignableFrom(clazz)) {
                return getValueType(type);
            } else if (clazz.isArray()) {
                if (SysConstant.SYS_LENGTH.equals(key)) {
                    return Integer.TYPE;
                } else if (Number.class.isInstance(key)) {
                    return clazz.getComponentType();
                }
            } else {
                Type[] pd = getTypeMap(clazz).get(String.valueOf(key));
                if (pd != null) {
                    return getParameterizedType(type, (Class<?>) pd[1], pd[0]);
                }
            }
        }
        return null;
    }

    public static Object getValue(Object context, Object key) {
        Class<? extends Object> clazz = context.getClass();
        if (context != null) {
            try {
                if (context instanceof Map<?, ?>) {
                    return ((Map<?, ?>) context).get(key);
                }
                if (key instanceof String) {
                    Method method = getGetterMap(clazz).get(key);
                    if (method == null) {
                        Field field;
                        if (context instanceof Class<?>) {
                            field = getFieldMap((Class<?>) context).get(key);
                        } else {
                            field = getFieldMap(clazz).get(key);

                        }
                        if (field != null) {
                            return field.get(context);
                        }
                    } else {
                        return method.invoke(context);
                    }
                    if (SysConstant.SYS_LENGTH.equals(key)) {
                        if (clazz.isArray()) {
                            return Array.getLength(context);
                        } else if (context instanceof Collection<?>) {
                            return ((Collection<?>) context).size();
                        } else if (context instanceof String) {
                            return ((String) context).length();
                        }
                    }
                    if (context instanceof Map<?, ?>) {
                        return ((Map<?, ?>) context).get(key);
                    } else if (clazz.isArray()) {
                        return Array.getLength(context);
                    } else {
                        return Array.get(context, toIndex(key));
                    }
                } else if (context instanceof List<?>) {
                    return ((List<?>) context).get(toIndex(key));
                }

            } catch (Exception e) {
                throw new FastElException(e);
            }
        }
        return null;
    }


    public static void setValue(Object base, Object key, Object value) {
        if (base != null) {
            try {
                Class<? extends Object> clazz = base.getClass();
                if (clazz.isArray()) {
                    Array.set(base, toIndex(key), value);
                } else if (base instanceof List<?>) {
                    ((List<Object>) base).set(toIndex(key), value);
                }
                if (base instanceof Map) {
                    ((Map<Object, Object>) base).put(key, value);
                }
                String name = String.valueOf(key);
                Method method = getSetterMap(clazz).get(name);
                if (method != null) {
                    if (value != null) {
                        Class<?> type = method.getParameterTypes()[0];
                        value = toWrapper(value, type);
                    }
                    method.invoke(base, value);
                } else {
                    Field field = FIELD_MAP.get(clazz).get(name);
                    if (value != null) {
                        Class<?> type = field.getType();
                        value = toWrapper(value, type);
                    }
                    field.set(base, value);
                }
            } catch (Exception e) {
                throw new FastElException(e);
            }
        }

    }

    private static Object toWrapper(Object value, Class<?> type) {
        if (!type.isInstance(value)) {
            type = toWrapper(type);
            if (Number.class.isAssignableFrom(type)) {
                value = toValue((Number) value, type);
            }
        }
        return value;
    }

    public static Number toValue(Number value, Class<? extends Object> type) {
        if (type == Long.class) {
            return value.longValue();
        } else if (type == Integer.class) {
            return value.intValue();
        } else if (type == Short.class) {
            return value.shortValue();
        } else if (type == Byte.class) {
            return value.byteValue();
        } else if (type == Double.class) {
            return value.doubleValue();
        } else if (type == Float.class) {
            return value.floatValue();
        } else {
            Class<? extends Object> clazz = ReflectUtil.toWrapper(type);
            if (clazz == type) {
                return null;
            } else {
                return toValue(value, clazz);
            }
        }
    }

    public final static Class<? extends Object> toWrapper(
            Class<? extends Object> type) {
        if (type.isPrimitive()) {
            if (Byte.TYPE == type) {
                return Byte.class;
            } else if (Short.TYPE == type) {
                return Short.class;
            } else if (Integer.TYPE == type) {
                return Integer.class;
            } else if (Long.TYPE == type) {
                return Long.class;
            } else if (Float.TYPE == type) {
                return Float.class;
            } else if (Double.TYPE == type) {
                return Double.class;
            } else if (Character.TYPE == type) {
                return Character.class;
            } else if (Boolean.TYPE == type) {
                return Boolean.class;
            }
        }
        return type;
    }
}
