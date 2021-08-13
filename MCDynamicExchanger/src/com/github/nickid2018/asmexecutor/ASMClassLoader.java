package com.github.nickid2018.asmexecutor;

public final class ASMClassLoader extends ClassLoader {

    public static final ASMClassLoader LOADER = new ASMClassLoader();

    public Class<?> makeClass(byte[] asms, String name) {
        return defineClass(name, asms, 0, asms.length);
    }
}
