package io.github.nickid2018.asmexecutor.gen;

import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.Locale;

public enum AccessFlagType {
    
    CLASS,
    METHOD,
    FIELD,
    PARAMETER;

    public static int flagsToInt(String flags, AccessFlagType type) throws IOException {
        String[] split = flags.split(",");
        int accessFlags = 0;
        for(String str : split) {
            switch (str.toLowerCase(Locale.ROOT)) {
                case "public":
                    if(type == PARAMETER)
                        throw new IOException("Public access flag can't be used in parameter type.");
                    accessFlags |= Opcodes.ACC_PUBLIC;
                    break;
                case "protected":
                    if(type == PARAMETER)
                        throw new IOException("Protected access flag can't be used in parameter type.");
                    accessFlags |= Opcodes.ACC_PROTECTED;
                    break;
                case "private":
                    if(type == PARAMETER)
                        throw new IOException("Private access flag can't be used in parameter type.");
                    accessFlags |= Opcodes.ACC_PRIVATE;
                    break;
                case "static":
                    if(type == PARAMETER)
                        throw new IOException("Static access flag can't be used in parameter type.");
                    accessFlags |= Opcodes.ACC_STATIC;
                    break;
                case "final":
                    accessFlags |= Opcodes.ACC_FINAL;
                    break;
            }
        }
        return accessFlags;
    }
}
