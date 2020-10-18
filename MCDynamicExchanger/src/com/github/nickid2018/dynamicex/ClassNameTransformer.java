package com.github.nickid2018.dynamicex;

import java.io.*;
import java.net.*;
import java.util.jar.*;
import com.google.gson.*;
import org.objectweb.asm.*;
import net.minecraft.client.*;
import com.github.nickid2018.util.*;
import org.objectweb.asm.commons.*;
import com.github.nickid2018.mcremap.*;
import com.github.nickid2018.mcremap.argparser.*;

public class ClassNameTransformer {

	public static RemapClassSerializeHelper helper;
	public static PlainClassRemapper remapper;

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

	public static String getFieldName(String className, String fieldName) {
		return helper == null || !helper.classes.containsKey(className) ? fieldName
				: helper.classes.get(className).findField(fieldName);
	}

	public static String findSourceName(String className) {
		String ret = className;
		if (!ClassNameTransformer.isRemapped())
			for (RemapClass clazz : helper.classes.values()) {
				if (clazz.remapName.equals(className)) {
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

	private static void doDeserialize(String file) throws IOException, ClassNotFoundException {
		System.out.println("Finded right version of mapping: " + file);
		helper.doDeserialize("dynamicexchanger/mappings/" + file);
		System.out.println("Generated class mapping in memory.");
	}

	private static String getVersionName() {
		Reader reader = new InputStreamReader(ClassNameTransformer.class.getResourceAsStream("/version.json"));
		JsonElement element = new JsonParser().parse(reader);
		return element.getAsJsonObject().get("id").getAsString();
	}

	private static String getDownloadMapName() throws IOException {
		String path = getMinecraftPath();
		Reader reader = new FileReader(path.substring(0, path.length() - 4) + ".json");
		JsonElement element = new JsonParser().parse(reader);
		JsonObject download = element.getAsJsonObject().getAsJsonObject("downloads").getAsJsonObject("client_mappings");
		return download.get("url").getAsString();
	}

	private static String getMinecraftPath() throws IOException {
		JarURLConnection connection = (JarURLConnection) ClassNameTransformer.class
				.getResource("/net/minecraft/client/ClientBrandRetriever.class").openConnection();
		JarFile file = connection.getJarFile();
		return file.getName();
	}

	private static void doMapGenerate(String path) throws Exception {
		RemapperMain.logger = new DefaultConsoleLogger();
		CommandResult result = new CommandResult();
		result.putSwitch("mc_file", new StringArgumentSwitch("mc_file").setValue(getMinecraftPath()));
		result.putSwitch("--outrev",
				new StringArgumentSwitch("--outrev").setValue(path.substring(0, path.length() - 4) + ".map"));
		System.out.println("Generate Mappings");
		OfficalFormat format = new OfficalFormat(result);
		format.processInitMap("file:" + new File(path).getAbsolutePath());
		ClassTreeGenerator gen = new ClassTreeGenerator();
		gen.runGenerate(result, format);
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
				boolean finded = false;
				for (String file : new File("dynamicexchanger/mappings").list()) {
					if (!file.endsWith(".map"))
						continue;
					String now = helper.tryMD5("dynamicexchanger/mappings/" + file);
					if (now.equals(md5)) {
						doDeserialize(file);
						finded = true;
						break;
					}
				}
				if (!finded) {
					String versionName = getVersionName();
					String name = "dynamicexchanger/mappings/" + versionName + ".txt";
					if (!new File(name).exists())
						if (!DownloadUtils.downloadResource(getDownloadMapName(), name)) {
							System.err.println("Resource downloading failed, please restrart the program!");
							System.exit(0);
						}
					doMapGenerate(name);
					doDeserialize(versionName + ".map");
				}
				remapper = new PlainClassRemapper(helper.classes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
