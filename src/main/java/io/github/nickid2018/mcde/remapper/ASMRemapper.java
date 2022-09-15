package io.github.nickid2018.mcde.remapper;

import io.github.nickid2018.mcde.format.MappingClassData;
import org.objectweb.asm.commons.Remapper;

import java.util.Map;

public class ASMRemapper extends Remapper {

    private final Map<String, MappingClassData> classMap;

    public ASMRemapper(Map<String, MappingClassData> classMap) {
        this.classMap = classMap;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        MappingClassData clazz = classMap.get(toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findMethod(name + desc);
        return get == null ? name : get;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        MappingClassData clazz = classMap.get(toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findField(name + "+" + desc);
        return get == null ? name : get;
    }

    @Override
    public String map(String typeName) {
        MappingClassData clazz = classMap.get(toBinaryName(typeName));
        return clazz == null ? typeName : toInternalName(clazz.mapName());
    }

    public Map<String, MappingClassData> getClassMap() {
        return classMap;
    }

    public static String toBinaryName(String name) {
        return name.replace('/', '.');
    }

    public static String toInternalName(String name) {
        return name.replace('.', '/');
    }
}