package io.github.nickid2018.asmexecutor;

import java.util.Set;
import java.util.TreeSet;

public final class ASMClassHolder {

    private static final Set<ASMClass> classes = new TreeSet<>();

    public static void addClass(ASMClass clazz) {
        classes.add(clazz);
    }

    public static void deleteClass(ASMClass clazz) {
        classes.remove(clazz);
    }
}
