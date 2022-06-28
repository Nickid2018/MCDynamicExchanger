package io.github.nickid2018.asmexecutor.gen;

import org.objectweb.asm.ClassWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;

public class ClassMaker {

    private final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    private final BufferedReader reader;

    public ClassMaker(BufferedReader reader){
        this.reader = reader;
    }

    public ClassMaker analyze() throws IOException {
        analyzeHead();
        return this;
    }

    private void analyzeHead() throws IOException {
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
        int accessFlag = 0;
        String line;
        while(!(line = reader.readLine()).equalsIgnoreCase("defined")) {
            String[] split = line.split(" ");
            switch (split[0].toLowerCase(Locale.ROOT)){
                case "class":
                    className = split[1];
                    break;
                case "extends":
                    superName = split[1];
                    break;
                case "implement":
                    interfaces.add(split[1]);
                    break;
                case "type":
                    signature = split[1];
                    break;

                default:
                    throw new IOException("Unrecognized token at class definition: " + split[0]);
            }
        }
    }
}
