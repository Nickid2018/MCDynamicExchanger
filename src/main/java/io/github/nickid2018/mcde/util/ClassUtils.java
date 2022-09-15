package io.github.nickid2018.mcde.util;

import java.util.Map;

public class ClassUtils {
    public static String mapSignature(String str, Map<String, String> revClass) {
        if (str.indexOf('[') >= 0) {
            String[] sp = str.split("\\[");
            return "[".repeat(sp.length - 1) + mapSignature(sp[0], revClass);
        }
        return switch (str) {
            case "int" -> "I";
            case "float" -> "F";
            case "double" -> "D";
            case "long" -> "J";
            case "boolean" -> "Z";
            case "short" -> "S";
            case "byte" -> "B";
            case "char" -> "C";
            case "void" -> "V";
            default -> "L" + revClass.getOrDefault(str, str).replace('.', '/') + ";";
        };
    }

    public static String toBinaryName(String name) {
        return name.replace('/', '.');
    }

    public static String toInternalName(String name) {
        return name.replace('.', '/');
    }
}
