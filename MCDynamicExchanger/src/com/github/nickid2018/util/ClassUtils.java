package com.github.nickid2018.util;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import com.github.nickid2018.dynamicex.*;

public class ClassUtils {

	public static final byte[] getClassBytes(String className, boolean isRedirectable) throws IOException {
		return isRedirectable && DynamicClassesHolder.isCovered(className)
				? DynamicClassesHolder.getCoveredClass(className).definition.getDefinitionClassFile()
				: IOUtils.toByteArray(
						ClassUtils.class.getResourceAsStream("/" + ClassUtils.toInternalName(className) + ".class"));
	}

	public static final String toBinaryName(String name) {
		return name.replace('/', '.');
	}

	public static final String toInternalName(String name) {
		return name.replace('.', '/');
	}

	public static final String mapToSig(String str, Map<String, String> revClass) {
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
}
