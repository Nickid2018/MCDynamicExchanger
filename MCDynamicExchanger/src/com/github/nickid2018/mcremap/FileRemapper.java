package com.github.nickid2018.mcremap;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.function.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;
import org.objectweb.asm.commons.*;

public class FileRemapper {

	public static String tmpLocation = System.getProperty("java.io.tmpdir") + "\\MC-Remap";

	private String nowFile;
	private double dealed = 0;
	private double all;

	public void remapAll(String location, RemapperFormat format, String dest) throws Exception {
		ZipFile file = new ZipFile(new File(location));
		all = file.size() * 3;// Run 3 times
		cleanUpDirectory();
		// Generate Class Extends Tree
		addPlainClasses(file, format);
		generateExtendTree(file, format);
		// Remap
		remapAllClasses(file, format);
		file.close();
		// MANIFEST.MF
		recreateManifest();
		// Remove Modded Check
		removeModCheck();
		// Change Brand
		changeBrand();
		// zhuang beta de dai ma
		hackTheName();
		System.out.println("Class mapping over, now start to pack it into JAR. Please wait jar.exe packing the files.");
		runPack(dest);
	}

	private static final void cleanUpDirectory() throws IOException {
		FileUtils.deleteDirectory(new File(tmpLocation));
	}

	private final void addPlainClasses(ZipFile file, RemapperFormat format) throws IOException {
		Enumeration<? extends ZipEntry> entries = file.entries();
		while (entries.hasMoreElements()) {
			dealed++;
			ZipEntry entry = entries.nextElement();
			if (!(nowFile = entry.getName()).endsWith(".class"))
				continue;
			ClassReader reader = new ClassReader(IOUtils.toByteArray(file.getInputStream(entry)));
			String className = reader.getClassName().replace('/', '.');
			RemapClass clazz = format.remaps.get(className);
			if (clazz == null) {
				ClassNode node = new ClassNode(Opcodes.ASM6);
				reader.accept(node, ClassReader.SKIP_CODE);
				clazz = new RemapClass(className, className);
				format.remaps.put(className, clazz);
				for (Object mno : node.methods) {
					MethodNode mn = (MethodNode) mno;
					clazz.methodMappings.put(mn.name + mn.desc, mn.name);
				}
				for (Object flo : node.fields) {
					FieldNode fl = (FieldNode) flo;
					clazz.fieldMappings.put(fl.name, fl.name);
				}
			}
		}
	}

	private final void generateExtendTree(ZipFile file, RemapperFormat format) throws IOException {
		Enumeration<? extends ZipEntry> entries = file.entries();
		while (entries.hasMoreElements()) {
			dealed++;
			ZipEntry entry = entries.nextElement();
			if (!(nowFile = entry.getName()).endsWith(".class"))
				continue;
			ClassReader reader = new ClassReader(IOUtils.toByteArray(file.getInputStream(entry)));
			RemapClass clazz = format.remaps.get(ClassUtils.toBinaryName(reader.getClassName()));
			clazz.superClasses.add(format.remaps.get(ClassUtils.toBinaryName(reader.getSuperName())));
			for (String name : reader.getInterfaces())
				clazz.superClasses.add(format.remaps.get(ClassUtils.toBinaryName(name)));
		}
	}

	private final void remapAllClasses(ZipFile file, RemapperFormat format) throws IOException {
		Enumeration<? extends ZipEntry> entries = file.entries();
		ASMRemapper remapper = new ASMRemapper(format);
		while (entries.hasMoreElements()) {
			dealed++;
			ZipEntry entry = entries.nextElement();
			if (entry.isDirectory() || entry.getName().startsWith("META-INF"))
				continue;
			byte[] bytes = IOUtils.toByteArray(file.getInputStream(entry));
			if (!(nowFile = entry.getName()).endsWith(".class")) {
				write(nowFile, bytes);
				continue;
			}
			ClassReader reader = new ClassReader(bytes);
			ClassWriter writer = new ClassWriter(0);
			reader.accept(new ClassRemapper(writer, remapper), 0);
			String className = entry.getName().replace('/', '.');
			className = className.substring(0, className.length() - 6);
			write(format.remaps.get(className).mapName().replace('.', '\\') + ".class", writer.toByteArray());
		}
	}

	private static final void recreateManifest() throws IOException {
		File tmpMETA_INF = new File(tmpLocation, "META-INF");
		tmpMETA_INF.mkdir();
		File tmpMF = new File(tmpMETA_INF, "MANIFEST.MF");
		FileWriter writer = new FileWriter(tmpMF);
		IOUtils.write("Manifest-Version: 1.0\r\n" + "Main-Class: net.minecraft.client.main.Main\r\n" + "", writer);
		writer.close();
	}

	private static final void write(String file, byte[] in) throws IOException {
		File create = new File(tmpLocation + "\\" + file.replace('/', '\\'));
		if (!create.getParentFile().isDirectory())
			create.getParentFile().mkdirs();
		create.createNewFile();
		OutputStream os = new FileOutputStream(create);
		IOUtils.write(in, os);
		os.close();
	}

	private void removeModCheck() throws IOException {
		doHacks("net.minecraft.client.Minecraft", reader -> {
			ClassWriter writer = new ClassWriter(0);
			reader.accept(new RemoveModCheck(writer), 0);
			return writer;
		});
	}

	private void hackTheName() throws IOException {
		doHacks("net.minecraft.client.gui.screens.TitleScreen", reader -> {
			ClassWriter writer = new ClassWriter(0);
			reader.accept(new ModifyTitleScreen(writer), 0);
			return writer;
		});
	}

	private void changeBrand() throws IOException {
		Function<ClassReader, ClassWriter> func = reader -> {
			ClassWriter writer = new ClassStringReplacer("vanilla", "remapped by Nickid2018");
			reader.accept(writer, 0);
			return writer;
		};
		doHacks("net.minecraft.server.MinecraftServer", func);
		doHacks("net.minecraft.client.ClientBrandRetriever", func);
	}

	private static void doHacks(String className, Function<ClassReader, ClassWriter> func) throws IOException {
		InputStream is = new FileInputStream(tmpLocation + "\\" + className.replace('.', '\\') + ".class");
		ClassReader reader = new ClassReader(IOUtils.toByteArray(is));
		is.close();
		ClassWriter writer = func.apply(reader);
		OutputStream os = new FileOutputStream(tmpLocation + "\\" + className.replace('.', '\\') + ".class");
		IOUtils.write(writer.toByteArray(), os);
		os.close();
	}

	public static void runPack(String dest) throws IOException, InterruptedException {
		Runtime.getRuntime()
				.exec("jar cvfm " + dest + " " + tmpLocation + "\\META-INF\\MANIFEST.MF -C " + tmpLocation + " .");
		System.out.println("Minecraft has been remapped successfully.");
	}

	public String getNowFile() {
		return nowFile;
	}

	public double getProgress() {
		return dealed / all;
	}
}
