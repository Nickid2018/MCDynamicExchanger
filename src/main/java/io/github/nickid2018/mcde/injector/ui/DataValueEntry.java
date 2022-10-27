package io.github.nickid2018.mcde.injector.ui;

import com.google.gson.internal.LinkedTreeMap;
import io.github.nickid2018.mcde.remapper.ASMRemapper;
import io.github.nickid2018.mcde.util.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class DataValueEntry {

    private final String name;
    private Object value;

    private final Class<?> clazz;

    private final String className;
    private final ASMRemapper remapper;
    private final Map<String, Field> fields = new HashMap<>();

    public DataValueEntry(String name, Object obj, ASMRemapper remapper) {
        this.name = name;
        this.value = obj;
        this.remapper = remapper;

        if (obj == null) {
            clazz = null;
            className = null;
            return;
        }

        Class<?> clazz = obj.getClass();
        this.clazz = clazz;

        if (clazz.isArray()) {
            int dimension = 1;
            while ((clazz = clazz.getComponentType()).isArray())
                dimension++;
            String type = remapper.map(clazz.getName());
            String[] split = type.split("[/.]");
            className = split[split.length - 1] + "[]".repeat(dimension);
        } else if (!Collection.class.isAssignableFrom(clazz)) {
            String type = remapper.map(clazz.getName());
            String[] split = type.split("[/.]");
            className = split[split.length - 1];
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers()))
                        continue;
                    try {
                        field.setAccessible(true);
                        fields.put(remapper.mapFieldNameNoType(ClassUtils.toInternalName(clazz.getName()), field.getName()), field);
                    } catch (Exception ignored) {
                    }
                }
                clazz = clazz.getSuperclass();
            }
        } else {
            String type = remapper.map(clazz.getName());
            String[] split = type.split("[/.]");
            className = split[split.length - 1];
        }
    }

    @Override
    public String toString() {
        try {
            return className == null ? name + ":" + value : name + ":" + className + ":" + value;
        } catch (ConcurrentModificationException e) {
            return name + ":" + className + ":(concurrent modification when getting value)";
        }
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Map<String, DataValueEntry> getSubObjects() {
        if (value == null)
            return new HashMap<>();
        Map<String, DataValueEntry> map = new LinkedHashMap<>();
        if (Collection.class.isAssignableFrom(clazz)) {
            Collection<?> collection = (Collection<?>) value;
            int size = Math.min(collection.size(), 40);
            Object[] data = new Object[size];
            collection.toArray(data);
            map.put("<size>", new DataValueEntry("<size>", collection.size(), remapper));
            for (int i = 0; i < size; i++)
                map.put("[" + i + "]", new DataValueEntry("[" + i + "]", data[i], remapper));
            if (collection.size() > 40)
                map.put("...", new DataValueEntry("...", null, remapper));
        } else if (!clazz.isArray()) {
            for (Map.Entry<String, Field> entry : fields.entrySet()) {
                try {
                    map.put(entry.getKey(), new DataValueEntry(entry.getKey(), entry.getValue().get(value), remapper));
                } catch (IllegalAccessException ignored) {
                }
            }
        } else {
            int length = Array.getLength(value);
            for (int i = 0; i < length && i < 40; i++)
                map.put("[" + i + "]", new DataValueEntry("[" + i + "]", Array.get(value, i), remapper));
            if (length > 40)
                map.put("...", new DataValueEntry("...", null, remapper));
        }
        return map;
    }
}
