package com.github.nickid2018.asmexecutor;

import org.objectweb.asm.ClassReader;

import java.util.Arrays;

public final class ASMClass implements Comparable<ASMClass> {

    public final int classHash;
    public final Class<?> clazz;

    public ASMClass(byte[] asmcodes, String name) {
        classHash = Arrays.hashCode(asmcodes);
        clazz = ASMClassLoader.LOADER.makeClass(asmcodes, name);
        ASMClassHolder.addClass(this);
    }

    public static ASMClass newClass(byte[] asmcodes) {
        ClassReader reader = new ClassReader(asmcodes);
        return new ASMClass(asmcodes, reader.getClassName());
    }

    @Override
    public int compareTo(ASMClass o) {
        return classHash - o.classHash;
    }
}
