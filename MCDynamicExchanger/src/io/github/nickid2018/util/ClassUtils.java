package io.github.nickid2018.util;

import io.github.nickid2018.mcremap.RemapClass;
import io.github.nickid2018.mcremap.RemapperFormat;

import java.util.Map;

public class ClassUtils {

    public static String toBinaryName(String name) {
        return name.replace('/', '.');
    }

    public static String toInternalName(String name) {
        return name.replace('.', '/');
    }

    public static String mapToSig(String str, Map<String, String> revClass) {
        if (str.indexOf('[') >= 0) {
            String[] sp = str.split("\\[");
            String base = mapToSig(sp[0], revClass);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sp.length - 1; i++)
                sb.append('[');
            sb.append(base);
            return sb.toString();
        }
        switch (str) {
            case "int":
                return "I";
            case "float":
                return "F";
            case "double":
                return "D";
            case "long":
                return "J";
            case "boolean":
                return "Z";
            case "short":
                return "S";
            case "byte":
                return "B";
            case "char":
                return "C";
            case "void":
                return "V";
            default:
                return "L" + revClass.getOrDefault(str, str).replace('.', '/') + ";";
        }
    }

    public static String remapSig(String str, RemapperFormat format) {
        StringBuilder sb = new StringBuilder();
        boolean isClass = false;
        int startClassChar = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case 'I':
                case 'F':
                case 'D':
                case 'J':
                case 'Z':
                case 'S':
                case 'B':
                case 'C':
                case 'V':
                case '[':
                case '(':
                case ')':
                    if (!isClass)
                        sb.append(ch);
                    break;
                case 'L':
                    if (!isClass)
                        startClassChar = i + 1;
                    isClass = true;
                    break;
                case ';':
                    String name = str.substring(startClassChar, i);
                    RemapClass clazz = format.remaps.get(toBinaryName(name));
                    sb.append("L").append(toInternalName(clazz == null || clazz.remapName == null ? name : clazz.remapName)).append(";");
                    isClass = false;
            }
        }
        return sb.toString();
    }
}
