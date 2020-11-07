package com.github.nickid2018.mcremap;

import java.io.*;
import java.util.*;
import com.github.nickid2018.util.*;

public class RemapClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1256373621833338962L;

	public String sourceName;
	public String remapName;
	public Set<RemapClass> superClasses;
	public Map<String, String> fieldMappings;
	public Map<String, String> methodMappings;
	public RemapperFormat format;

	public RemapClass(String sourceName, String remapName, RemapperFormat format) {
		this.sourceName = sourceName;
		this.remapName = remapName;
		this.format = format;
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

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		superClasses = new HashSet<>();
		sourceName = ois.readUTF();
		remapName = ois.readUTF();
		fieldMappings = (Map<String, String>) ois.readObject();
		methodMappings = (Map<String, String>) ois.readObject();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		if (RemapClassSerializeHelper.getInstance().reverse) {
			oos.writeUTF(remapName);
			oos.writeUTF(sourceName);
			oos.writeObject(reverseStringMap(fieldMappings));
			oos.writeObject(reverseMethodMap(methodMappings));
			String[] sups = superClasses.stream().filter(clazz -> clazz != null).map(clazz -> clazz.remapName)
					.toArray(String[]::new);
			RemapClassSerializeHelper.getInstance().inheritTree.put(remapName, sups);
		} else {
			oos.writeUTF(sourceName);
			oos.writeUTF(remapName);
			oos.writeObject(fieldMappings);
			oos.writeObject(methodMappings);
			String[] sups = superClasses.stream().filter(clazz -> clazz != null).map(clazz -> clazz.sourceName)
					.toArray(String[]::new);
			RemapClassSerializeHelper.getInstance().inheritTree.put(sourceName, sups);
		}
	}

	private Map<String, String> reverseStringMap(Map<String, String> map) {
		Map<String, String> rev = new HashMap<>();
		map.forEach((s1, s2) -> {
			rev.put(s2, s1);
		});
		return rev;
	}

	private Map<String, String> reverseMethodMap(Map<String, String> map) {
		Map<String, String> rev = new HashMap<>();
		map.forEach((s1, s2) -> {
			String[] split = s1.split("\\(", 2);
			rev.put(s2 + "(" + ClassUtils.remapSig(split[1], format), split[0]);
		});
		return rev;
	}
}
