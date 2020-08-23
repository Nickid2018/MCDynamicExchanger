package com.github.nickid2018.dynamicex;

import java.lang.instrument.*;

public class DynamicClass {

	public final String filePath;
	public final ClassDefinition definition;

	public DynamicClass(String filePath, ClassDefinition definition) {
		this.filePath = filePath;
		this.definition = definition;
	}
}
