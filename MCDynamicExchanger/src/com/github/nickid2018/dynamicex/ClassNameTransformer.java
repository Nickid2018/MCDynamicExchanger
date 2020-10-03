package com.github.nickid2018.dynamicex;

import java.io.*;
import org.objectweb.asm.*;
import net.minecraft.client.*;
import com.github.nickid2018.util.*;
import org.objectweb.asm.commons.*;
import com.github.nickid2018.mcremap.*;

public class ClassNameTransformer {

	private static RemapClassSerializeHelper helper;
	private static PlainClassRemapper remapper;

	public static boolean isRemapped() {
		return helper == null;
	}

	public static String getClassName(String className) {
		return helper == null || !helper.classes.containsKey(className) ? className
				: helper.classes.get(className).remapName;
	}

	public static String getResourceName(String className) {
		return ClassUtils.toInternalName(
				(helper == null || !helper.classes.containsKey(ClassUtils.toBinaryName(className)) ? className
						: helper.classes.get(ClassUtils.toBinaryName(className)).remapName));
	}

	public static String getQualifiedName(String className) {
		return "L" + ClassUtils.toInternalName(
				(helper == null || !helper.classes.containsKey(ClassUtils.toBinaryName(className)) ? className
						: helper.classes.get(ClassUtils.toBinaryName(className)).remapName))
				+ ";";
	}

	public static String getMethodName(String className, String methodName) {
		return helper == null || !helper.classes.containsKey(className) ? methodName.split("\\(")[0]
				: helper.classes.get(className).findMethod(methodName);
	}

	public static String findSourceName(String className) {
		String ret = className;
		if (!ClassNameTransformer.isRemapped())
			for (RemapClass clazz : helper.classes.values()) {
				if (clazz.remapName == className) {
					ret = clazz.sourceName;
					break;
				}
			}
		return ret;
	}

	public static byte[] changeClassData(byte[] bytes) {
		if (helper != null) {
			ClassWriter cw = new ClassWriter(0);
			ClassRemapper cr = new ClassRemapper(cw, remapper);
			ClassReader reader = new ClassReader(bytes);
			reader.accept(cr, 0);
			return cw.toByteArray();
		} else
			return bytes;
	}

	static {
		String type = ClientBrandRetriever.getClientModName();
		switch (type) {
		case "remapped":
			System.out.println("Recognized the client is \"remapped\" version, stop mapping read.");
			break;
		case "vanilla":
			try {
				System.out.println("Recognized the client is \"vanilla\" version.");
				helper = RemapClassSerializeHelper.getInstance();
				String md5 = MD5Compute.getMD5(ClassNameTransformer.class.getResourceAsStream("/META-INF/MOJANGCS.SF"));
				System.out.println("The MD5 of the SF file is " + md5);
				for (String file : new File("dynamicexchanger/mappings").list()) {
					String now = helper.tryMD5("dynamicexchanger/mappings/" + file);
					if (now.equals(md5)) {
						System.out.println("Finded right version of mapping: " + file);
						helper.doDeserialize("dynamicexchanger/mappings/" + file);
						System.out.println("Generated class mapping in memory.");
						break;
					}
				}
				remapper = new PlainClassRemapper(helper.classes);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
