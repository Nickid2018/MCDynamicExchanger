package io.github.nickid2018.mcde.format;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MappingClassData {
    public String sourceName;
    public String remapName;
    public Set<MappingClassData> superClasses;
    public Map<String, String> fieldMappings;
    public Map<String, String> methodMappings;

    public MappingClassData(String sourceName, String remapName) {
        this.sourceName = sourceName;
        this.remapName = remapName;
        superClasses = new HashSet<>();
        fieldMappings = new HashMap<>();
        methodMappings = new HashMap<>();
    }

    public final String findFieldNoDesc(String name) {
        for (String key : fieldMappings.keySet()) {
            if (key.startsWith(name + "+"))
                return fieldMappings.get(key);
        }
        return null;
    }

    public final String findField(String nameWithDesc) {
        if (fieldMappings.containsKey(nameWithDesc))
            return fieldMappings.get(nameWithDesc);
        for (MappingClassData clazz : superClasses) {
            if (clazz == null)
                continue;
            String ret = clazz.findField(nameWithDesc);
            if (ret != null)
                return ret;
        }
        return null;
    }

    public final String findMethod(String nameWithDesc) {
        if (methodMappings.containsKey(nameWithDesc))
            return methodMappings.get(nameWithDesc);
        for (MappingClassData clazz : superClasses) {
            if (clazz == null)
                continue;
            String ret = clazz.findMethod(nameWithDesc);
            if (ret != null)
                return ret;
        }
        return null;
    }

    public final String mapName() {
        return remapName;
    }
}
