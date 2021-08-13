package com.github.nickid2018.mcremap;

import com.github.nickid2018.util.ClassUtils;
import org.objectweb.asm.commons.Remapper;

public class ASMRemapper extends Remapper {

    private final RemapperFormat format;

    public ASMRemapper(RemapperFormat format) {
        this.format = format;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        RemapClass clazz = format.remaps.get(ClassUtils.toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findMethod(name + desc);
        return get == null ? name : get;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        RemapClass clazz = format.remaps.get(ClassUtils.toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findField(name + desc);
        return get == null ? name : get;
    }

    @Override
    public String map(String typeName) {
        RemapClass clazz = format.remaps.get(ClassUtils.toBinaryName(typeName));
        return clazz == null ? typeName : ClassUtils.toInternalName(clazz.mapName());
    }
}
