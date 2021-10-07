package io.github.nickid2018.asmexecutor.gen;

import org.objectweb.asm.ClassWriter;

import java.io.BufferedReader;
import java.util.Vector;

public class ClassMaker {

    private final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    private final BufferedReader reader;

    public ClassMaker(BufferedReader reader){
        this.reader = reader;
    }

    public ClassMaker analyze() {
        analyzeHead();
        return this;
    }

    private void analyzeHead() {
        // Define:
        // class Test
        // access public
        // extends java/lang/Object
        // implement .
        // type TT;
        // defined
        String className = null; // Required
        String superName = "java/lang/Object";
        Vector<String> interfaces = new Vector<>();
        String signature = null;

    }
}
