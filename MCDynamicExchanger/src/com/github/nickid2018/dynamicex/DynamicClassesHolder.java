package com.github.nickid2018.dynamicex;

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.charset.*;
import java.lang.instrument.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.asmexecutor.gen.*;

public class DynamicClassesHolder {

	private static Map<String, DynamicClass> dynamicClasses = new HashMap<>();

	public static final void exchangeClass(String className, String file)
			throws IOException, ClassNotFoundException, UnmodifiableClassException {
		InputStream is = new FileInputStream(file);
		byte[] bytes = IOUtils.toByteArray(is);
		is.close();
		ClassDefinition definition = new ClassDefinition(Class.forName(className), bytes);
		DEProgramInterface.instrumention.redefineClasses(definition);
		dynamicClasses.put(className, new DynamicClass("file:" + file, definition));
	}

	public static final void exchangeASMClass(String className, String file)
			throws IOException, ClassNotFoundException, UnmodifiableClassException {
		InputStream is = new FileInputStream(file);
		String details = IOUtils.toString(is, Charset.defaultCharset());
		ClassGenerator genasm = new ClassGenerator(details, ClassUtils.toInternalName(className));
		is.close();
		genasm.make();
		ClassDefinition definition = new ClassDefinition(Class.forName(className), genasm.generated);
		DEProgramInterface.instrumention.redefineClasses(definition);
		dynamicClasses.put(className, new DynamicClass("asmfile:" + file, definition));
	}

	public static final void exchangeClassURL(String className, String file)
			throws IOException, ClassNotFoundException, UnmodifiableClassException {
		InputStream is = new URL(file).openStream();
		byte[] bytes = IOUtils.toByteArray(is);
		is.close();
		ClassDefinition definition = new ClassDefinition(Class.forName(className), bytes);
		DEProgramInterface.instrumention.redefineClasses(definition);
		dynamicClasses.put(className, new DynamicClass("file-url:" + file, definition));
	}

	public static final void exchangeASMClassURL(String className, String file)
			throws IOException, ClassNotFoundException, UnmodifiableClassException {
		InputStream is = new URL(file).openStream();
		String details = IOUtils.toString(is, Charset.defaultCharset());
		ClassGenerator genasm = new ClassGenerator(details, ClassUtils.toInternalName(className));
		is.close();
		genasm.make();
		ClassDefinition definition = new ClassDefinition(Class.forName(className), genasm.generated);
		DEProgramInterface.instrumention.redefineClasses(definition);
		dynamicClasses.put(className, new DynamicClass("asmfile-url:" + file, definition));
	}

	public static final void exchangeClassData(String className, byte[] data)
			throws IOException, ClassNotFoundException, UnmodifiableClassException {
		ClassDefinition definition = new ClassDefinition(Class.forName(className), data);
		DEProgramInterface.instrumention.redefineClasses(definition);
		dynamicClasses.put(className, new DynamicClass("data:" + className, definition));
	}

	public static final void recoverFile(String className)
			throws IOException, ClassNotFoundException, UnmodifiableClassException {
		// Find Details
		if (!dynamicClasses.containsKey(className))
			throw new IllegalArgumentException("The class hasn't been replaced!");
		InputStream is = DynamicClassesHolder.class
				.getResourceAsStream("/" + ClassUtils.toInternalName(className) + ".class");
		byte[] bytes = IOUtils.toByteArray(is);
		is.close();
		ClassDefinition definition = new ClassDefinition(Class.forName(className), bytes);
		DEProgramInterface.instrumention.redefineClasses(definition);
		dynamicClasses.remove(className);
	}

	public static final boolean isCovered(String className) {
		return dynamicClasses.containsKey(className);
	}

	public static final DynamicClass getCoveredClass(String className) {
		return dynamicClasses.get(className);
	}

	public static final Set<String> listInNames() {
		return dynamicClasses.keySet();
	}

	public static final Map<String, DynamicClass> list() {
		return dynamicClasses;
	}
}
