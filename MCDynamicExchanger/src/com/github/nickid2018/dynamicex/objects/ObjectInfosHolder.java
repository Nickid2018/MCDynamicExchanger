package com.github.nickid2018.dynamicex.objects;

import java.util.*;

public class ObjectInfosHolder {

	public static Map<String, ObjectElement> elements = new HashMap<>();

	// (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z
	public static boolean putInfo(String to, Object from, String name, Object value) {
		return elements.containsKey(to) && elements.get(to).putInformation(from, name, value);
	}

	// (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;I)Z
	public static boolean putInfo(String to, Object from, String name, int value) {
		return elements.containsKey(to) && elements.get(to).putInformation(from, name, value);
	}

	// (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;L)Z
	public static boolean putInfo(String to, Object from, String name, long value) {
		return elements.containsKey(to) && elements.get(to).putInformation(from, name, value);
	}

	// (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;F)Z
	public static boolean putInfo(String to, Object from, String name, float value) {
		return elements.containsKey(to) && elements.get(to).putInformation(from, name, value);
	}

	// (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;B)Z
	public static boolean putInfo(String to, Object from, String name, byte value) {
		return elements.containsKey(to) && elements.get(to).putInformation(from, name, value);
	}

	// (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;S)Z
	public static boolean putInfo(String to, Object from, String name, short value) {
		return elements.containsKey(to) && elements.get(to).putInformation(from, name, value);
	}

	// (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;D)Z
	public static boolean putInfo(String to, Object from, String name, double value) {
		return elements.containsKey(to) && elements.get(to).putInformation(from, name, value);
	}

	// (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Z)Z
	public static boolean putInfo(String to, Object from, String name, boolean value) {
		return elements.containsKey(to) && elements.get(to).putInformation(from, name, value);
	}
}
