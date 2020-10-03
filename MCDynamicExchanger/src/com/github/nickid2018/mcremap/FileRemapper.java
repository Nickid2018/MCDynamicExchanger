package com.github.nickid2018.mcremap;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.function.*;
import org.objectweb.asm.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;
import org.objectweb.asm.commons.*;
import com.github.nickid2018.mcremap.argparser.*;

public class FileRemapper {

	public String tmpLocation;

	private String nowFile;
	private double dealed = 0;
	private double all;
	private boolean detail;

	public void remapAll(CommandResult result, RemapperFormat format) throws Exception {
		tmpLocation = result.getStringOrDefault("--tmpdir", System.getProperty("java.io.tmpdir") + "\\MC-Remap");
		detail = result.containsSwitch("-D");
		ZipFile file = new ZipFile(new File(result.getSwitch("mc_file").toString()));
		all = file.size();
		cleanUpDirectory();
		// Remap
		remapAllClasses(file, format);
		file.close();
		// MANIFEST.MF
		recreateManifest();
		if (!result.containsSwitch("-Nh")) {
			// Remove Modded Check
			removeModCheck();
			// Change Brand
			changeBrand();
			// Add Remapped Mark
			hackTheName();
		}
		RemapperMain.logger
				.info("Class remapping over, now start to pack it into JAR, please wait jar.exe packing the files");
		runPack(result.getStringOrDefault("--output", "remapped.jar"));
	}

	private final void cleanUpDirectory() throws IOException {
		FileUtils.deleteDirectory(new File(tmpLocation));
		RemapperMain.logger.info("Cleaned up temporary directory");
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
			byte[] array = writer.toByteArray();
			if (detail)
				RemapperMain.logger.info("Remapping class: " + className + " (Length:" + array.length + ")");
			write(format.remaps.get(className).mapName().replace('.', '\\') + ".class", array);
		}
		RemapperMain.logger.info("Remapped all classes");
	}

	private void recreateManifest() throws IOException {
		File tmpMETA_INF = new File(tmpLocation, "META-INF");
		tmpMETA_INF.mkdir();
		File tmpMF = new File(tmpMETA_INF, "MANIFEST.MF");
		FileWriter writer = new FileWriter(tmpMF);
		IOUtils.write("Manifest-Version: 1.0\r\n" + "Main-Class: net.minecraft.client.main.Main\r\n" + "", writer);
		writer.close();
		RemapperMain.logger.info("Recreated manifest");
	}

	private void write(String file, byte[] in) throws IOException {
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
		RemapperMain.logger.info("Removed modding check");
	}

	private void hackTheName() throws IOException {
		doHacks("net.minecraft.client.gui.screens.TitleScreen", reader -> {
			ClassWriter writer = new ClassWriter(0);
			reader.accept(new ModifyTitleScreen(writer), 0);
			return writer;
		});
		RemapperMain.logger.info("Modified Title Screen");
	}

	private void changeBrand() throws IOException {
		Function<ClassReader, ClassWriter> func = reader -> {
			ClassWriter writer = new ClassStringReplacer("vanilla", "remapped");
			reader.accept(writer, 0);
			return writer;
		};
		doHacks("net.minecraft.server.MinecraftServer", func);
		doHacks("net.minecraft.client.ClientBrandRetriever", func);
		RemapperMain.logger.info("Modified Brand Tag");
	}

	private void doHacks(String className, Function<ClassReader, ClassWriter> func) throws IOException {
		InputStream is = new FileInputStream(tmpLocation + "\\" + className.replace('.', '\\') + ".class");
		ClassReader reader = new ClassReader(IOUtils.toByteArray(is));
		is.close();
		ClassWriter writer = func.apply(reader);
		OutputStream os = new FileOutputStream(tmpLocation + "\\" + className.replace('.', '\\') + ".class");
		IOUtils.write(writer.toByteArray(), os);
		os.close();
	}

	public void runPack(String dest) throws IOException, InterruptedException {
		Runtime.getRuntime()
				.exec("jar cvfm " + dest + " " + tmpLocation + "\\META-INF\\MANIFEST.MF -C " + tmpLocation + " .");
		RemapperMain.logger.info("Minecraft has been remapped successfully.");
	}

	public String getNowFile() {
		return nowFile;
	}

	public double getProgress() {
		return dealed / all;
	}
}
