package com.github.nickid2018.mcremap.optimize;

import java.util.*;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class OptimizedMethodRemapper extends MethodRemapper {

	private static Map<String, String> NAME_MAP = new HashMap<>();
	private static Map<String, Integer> NAME_COUNT = new HashMap<>();
	private static LocalRenameMode mode;
	private static boolean lineNumberOutput;

	protected OptimizedMethodRemapper(MethodVisitor methodVisitor, Remapper remapper) {
		super(methodVisitor, remapper);
		NAME_MAP.clear();
		NAME_COUNT.clear();
	}

	public static void setMode(LocalRenameMode mode) {
		OptimizedMethodRemapper.mode = mode;
	}

	public static void setNoLineNumbers() {
		lineNumberOutput = false;
	}

	@Override
	public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end,
			int index) {
		descriptor = remapper.mapDesc(descriptor);
		signature = remapper.mapSignature(signature, true);
		if (!name.equals("this")) {
			switch (mode) {
			case ERASE:
				break;
			case TYPE_COUNT:
				if (NAME_MAP.containsKey(descriptor)) {
					int now = NAME_COUNT.get(descriptor) + 1;
					super.visitLocalVariable(NAME_MAP.get(descriptor) + now, descriptor, signature, start, end, index);
					NAME_COUNT.put(descriptor, now);
				} else {
					StringBuilder sb = new StringBuilder();
					// Count array dimension
					String copy = descriptor;
					int dimension = 0;
					for (; dimension < descriptor.length(); dimension++) {
						if (copy.charAt(dimension) != '[')
							break;
					}
					boolean isArray = true;
					if (dimension == 1)
						sb.append("array");
					else if (dimension > 1)
						sb.append("multi" + dimension);
					else
						isArray = false;
					// Type
					copy = copy.substring(dimension);
					if (copy.endsWith(";")) {
						copy = copy.substring(1, copy.length() - 1);
						String[] split = copy.split("/");
						String className = split[split.length - 1];
						if (!isArray) {
							char head = className.charAt(0);
							head = Character.toLowerCase(head);
							className = head + className.substring(1);
						}
						sb.append(className);
						if (NAME_MAP.containsKey(sb.toString()))
							sb.append("_" + (split.length > 1 ? split[split.length - 2] : ""));
					} else {
						switch (copy) {
						case "I":
							sb.append(isArray ? "Int" : "i");
							break;
						case "F":
							sb.append(isArray ? "Float" : "f");
							break;
						case "D":
							sb.append(isArray ? "Double" : "d");
							break;
						case "J":
							sb.append(isArray ? "Long" : "l");
							break;
						case "Z":
							sb.append(isArray ? "Boolean" : "bool");
							break;
						case "S":
							sb.append(isArray ? "Short" : "s");
							break;
						case "B":
							sb.append(isArray ? "Byte" : "b");
							break;
						case "C":
							sb.append(isArray ? "Char" : "c");
							break;
						}
					}
					name = sb.toString();
					NAME_MAP.put(descriptor, name);
					NAME_COUNT.put(descriptor, 0);
					super.visitLocalVariable(name, descriptor, signature, start, end, index);
				}
				break;
			case VAR_COUNT:
				super.visitLocalVariable("var" + index, descriptor, signature, start, end, index);
				break;
			case NONE:
				super.visitLocalVariable(name, descriptor, signature, start, end, index);
			}
		} else
			super.visitLocalVariable(name, descriptor, signature, start, end, index);
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		if (lineNumberOutput)
			super.visitLineNumber(line, start);
	}
}
