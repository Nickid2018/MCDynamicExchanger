package com.github.nickid2018.dynamicex;

import java.util.*;
import com.github.nickid2018.util.*;
import org.objectweb.asm.commons.*;
import com.github.nickid2018.mcremap.*;

public class PlainClassRemapper extends Remapper {

	private final Map<String, RemapClass> classes;

	public PlainClassRemapper(Map<String, RemapClass> classes) {
		this.classes = classes;
	}

	@Override
	public String mapMethodName(String owner, String name, String desc) {
		RemapClass clazz = classes.get(ClassUtils.toBinaryName(owner));
		if (clazz == null)
			return name;
		String get = clazz.findMethod(name + desc);
		return get == null ? name : get;
	}

	@Override
	public String mapFieldName(String owner, String name, String desc) {
		RemapClass clazz = classes.get(ClassUtils.toBinaryName(owner));
		if (clazz == null)
			return name;
		String get = clazz.findField(name);
		return get == null ? name : get;
	}

	@Override
	public String map(String typeName) {
		RemapClass clazz = classes.get(ClassUtils.toBinaryName(typeName));
		return clazz == null ? typeName : ClassUtils.toInternalName(clazz.mapName());
	}
}
