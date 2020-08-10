package com.github.nickid2018.mcremap;

import java.util.*;

public class RemapClass {

	public final String sourceName;
	public final String remapName;
	public final Set<RemapClass> superClasses;
	public final Map<String, String> fieldMappings;
	public final Map<String, String> methodMappings;

	public RemapClass(String sourceName, String remapName) {
		this.sourceName = sourceName;
		this.remapName = remapName;
		superClasses = new HashSet<>();
		fieldMappings = new HashMap<>();
		methodMappings = new HashMap<>();
	}

	public final String findField(String name) {
		if (fieldMappings.containsKey(name))
			return fieldMappings.get(name);
		for (RemapClass clazz : superClasses) {
			if (clazz == null)
				continue;
			String ret = clazz.findField(name);
			if (ret != null)
				return ret;
		}
		return null;
	}

	public final String findMethod(String nameWithDesc) {
		if (methodMappings.containsKey(nameWithDesc))
			return methodMappings.get(nameWithDesc);
		for (RemapClass clazz : superClasses) {
			if (clazz == null)
				continue;
			String ret = clazz.findMethod(nameWithDesc);
			if (ret != null)
				return ret;
		}
		return null;
	}

	public final String mapName() {
		return remapName;
	}
}
