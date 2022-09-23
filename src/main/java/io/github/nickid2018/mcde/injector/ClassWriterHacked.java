package io.github.nickid2018.mcde.injector;

import io.github.nickid2018.mcde.format.MappingClassData;
import io.github.nickid2018.mcde.remapper.ASMRemapper;
import io.github.nickid2018.mcde.util.ClassUtils;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ClassWriterHacked extends ClassWriter {

    private final ASMRemapper remapper;

    public ClassWriterHacked(int flags, ASMRemapper remapper) {
        super(flags);
        this.remapper = remapper;
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        ClassLoader classLoader = getClassLoader();
        Class<?> class1;
        try {
            class1 = Class.forName(ClassUtils.toBinaryName(remapper.map(ClassUtils.toBinaryName(type1))), false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new TypeNotPresentException(type1, e);
        }
        Class<?> class2;
        try {
            class2 = Class.forName(ClassUtils.toBinaryName(remapper.map(ClassUtils.toBinaryName(type2))), false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new TypeNotPresentException(type2, e);
        }
        if (class1.isAssignableFrom(class2)) {
            return type1;
        }
        if (class2.isAssignableFrom(class1)) {
            return type2;
        }
        if (class1.isInterface() || class2.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                class1 = class1.getSuperclass();
            } while (!class1.isAssignableFrom(class2));
            return class1.getName().replace('.', '/');
        }
    }
}
