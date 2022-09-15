package io.github.nickid2018.mcde.remapper;

import io.github.nickid2018.mcde.format.MappingClassData;
import io.github.nickid2018.mcde.util.ClassUtils;
import org.objectweb.asm.commons.Remapper;

import java.util.Map;

public class ASMRemapper extends Remapper {

    private final Map<String, MappingClassData> classMap;

    public ASMRemapper(Map<String, MappingClassData> classMap) {
        this.classMap = classMap;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        MappingClassData clazz = classMap.get(ClassUtils.toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findMethod(name + desc);
        return get == null ? name : get;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        MappingClassData clazz = classMap.get(ClassUtils.toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findField(name + "+" + desc);
        return get == null ? name : get;
    }

    @Override
    public String map(String typeName) {
        MappingClassData clazz = classMap.get(ClassUtils.toBinaryName(typeName));
        return clazz == null ? typeName : ClassUtils.toInternalName(clazz.mapName());
    }

    public Map<String, MappingClassData> getClassMap() {
        return classMap;
    }

}