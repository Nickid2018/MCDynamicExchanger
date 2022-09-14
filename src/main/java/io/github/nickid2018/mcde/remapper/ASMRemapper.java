package io.github.nickid2018.mcde.remapper;

import org.objectweb.asm.commons.Remapper;

public class ASMRemapper extends Remapper {

    private final MojangFormatRemapper format;

    public ASMRemapper(MojangFormatRemapper format) {
        this.format = format;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        RemapClass clazz = format.remaps.get(toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findMethod(name + desc);
        return get == null ? name : get;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        RemapClass clazz = format.remaps.get(toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findField(name + "+" + desc);
        return get == null ? name : get;
    }

    @Override
    public String map(String typeName) {
        RemapClass clazz = format.remaps.get(toBinaryName(typeName));
        return clazz == null ? typeName : toInternalName(clazz.mapName());
    }


    public static String toBinaryName(String name) {
        return name.replace('/', '.');
    }

    public static String toInternalName(String name) {
        return name.replace('.', '/');
    }
}