package io.github.nickid2018.asmifier;

import java.util.Vector;

public enum AccessFlagCategory {

    CLASS,
    FIELD,
    METHOD,
    PARAMETER,
    MODULE,
    MODULE_REQUIRES,
    MODULE_EXPORT;

    public static String fromIntToAccessFlag(int flag, AccessFlagCategory category) {
        Vector<String> flags = new Vector<>();

        if(hasFlag(flag, 0x0001) && category.ordinal() < 3)
            flags.add("ACC_PUBLIC");
        if(hasFlag(flag, 0x0002) && category.ordinal() < 3)
            flags.add("ACC_PRIVATE");
        if(hasFlag(flag, 0x0004) && category.ordinal() < 3)
            flags.add("ACC_PROTECTED");
        if(hasFlag(flag, 0x0008) && (category == FIELD || category == METHOD))
            flags.add("ACC_STATIC");
        if(hasFlag(flag, 0x0010) && category.ordinal() < 4)
            flags.add("ACC_FINAL");
        if(hasFlag(flag, 0x0020) && category == CLASS)
            flags.add("ACC_SUPER");
        if(hasFlag(flag, 0x0020) && category == METHOD)
            flags.add("ACC_SYNCHRONIZED");
        if(hasFlag(flag, 0x0020) && category == MODULE)
            flags.add("ACC_OPEN");
        if(hasFlag(flag, 0x0020) && category == MODULE_REQUIRES)
            flags.add("ACC_TRANSITIVE");
        if(hasFlag(flag, 0x0040) && category == FIELD)
            flags.add("ACC_VOLATILE");
        if(hasFlag(flag, 0x0040) && category == METHOD)
            flags.add("ACC_BRIDGE");
        if(hasFlag(flag, 0x0040) && category == MODULE_REQUIRES)
            flags.add("ACC_STATIC_PHASE");
        if(hasFlag(flag, 0x0080) && category == METHOD)
            flags.add("ACC_VARARGS");
        if(hasFlag(flag, 0x0080) && category == FIELD)
            flags.add("ACC_TRANSIENT");
        if(hasFlag(flag, 0x0100) && category == METHOD)
            flags.add("ACC_NATIVE");
        if(hasFlag(flag, 0x0200) && category == CLASS)
            flags.add("ACC_INTERFACE");
        if(hasFlag(flag, 0x0400) && (category == CLASS || category == METHOD))
            flags.add("ACC_ABSTRACT");
        if(hasFlag(flag, 0x0800) && category == METHOD)
            flags.add("ACC_STRICT");
        if(hasFlag(flag, 0x1000) && (category != MODULE))
            flags.add("ACC_SYNTHETIC");
        if(hasFlag(flag, 0x2000) && category == CLASS)
            flags.add("ACC_ANNOTATION");
        if(hasFlag(flag, 0x4000) && (category == CLASS || category == FIELD))
            flags.add("ACC_ENUM");
        if(hasFlag(flag, 0x8000) && (category != CLASS))
            flags.add("ACC_MANDATED");
        if(hasFlag(flag, 0x8000) && category == CLASS)
            flags.add("ACC_MODULE");
        if(hasFlag(flag, 0x10000) && category == CLASS)
            flags.add("ACC_RECORD");
        if(hasFlag(flag, 0x20000) && category.ordinal() < 3)
            flags.add("ACC_DEPRECATED");

        if(flags.isEmpty())
            return "0";
        StringBuilder builder = new StringBuilder();
        builder.append(flags.get(0));
        for(int i = 1; i < flags.size(); i++)
            builder.append(" + ").append(flags.get(i));
        return builder.toString();
    }

    private static boolean hasFlag(int flag, int value) {
        return (flag & value) != 0;
    }
}
