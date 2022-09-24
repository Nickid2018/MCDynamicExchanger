package io.github.nickid2018.mcde.injector;

import io.github.nickid2018.mcde.remapper.ASMRemapper;
import io.github.nickid2018.mcde.util.ClassUtils;
import org.objectweb.asm.ClassWriter;

public class ClassWriterHacked extends ClassWriter {

    private final ASMRemapper remapper;

    public ClassWriterHacked(int flags, ASMRemapper remapper) {
        super(flags);
        this.remapper = remapper;
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        return super.getCommonSuperClass(
                remapper.map(ClassUtils.toBinaryName(type1)),
                remapper.map(ClassUtils.toBinaryName(type2)));
    }
}
