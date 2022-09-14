package io.github.nickid2018.mcde.remapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemapClass {
    public String sourceName;
    public String remapName;
    public Set<RemapClass> superClasses;
    public Map<String, String> fieldMappings;
    public Map<String, String> methodMappings;

    public RemapClass(String sourceName, String remapName) {
        this.sourceName = sourceName;
        this.remapName = remapName;
        superClasses = new HashSet<>();
        fieldMappings = new HashMap<>();
        methodMappings = new HashMap<>();
    }

    public final String findField(String name) {
        if (fieldMappings.containsKey(name))
            return fieldMappings.get(name);
        for (RemapClass clazz : superClasses) {
            if (clazz == null)
                continue;
            String ret = clazz.findField(name);
            if (ret != null)
                return ret;
        }
        return null;
    }

    public final String findMethod(String nameWithDesc) {
        if (methodMappings.containsKey(nameWithDesc))
            return methodMappings.get(nameWithDesc);
        for (RemapClass clazz : superClasses) {
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
