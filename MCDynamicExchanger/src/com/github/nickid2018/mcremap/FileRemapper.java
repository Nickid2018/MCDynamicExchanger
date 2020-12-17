package com.github.nickid2018.mcremap;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.function.*;
import org.objectweb.asm.*;
import com.github.nickid2018.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;
import org.objectweb.asm.commons.*;
import java.io.ByteArrayOutputStream;
import com.github.nickid2018.argparser.*;

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
		// Remap
		remapAllClasses(file, new ASMRemapper(format), format);
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
		runPack(result.getStringOrDefault("--output", "remapped.jar"));
	}

	private final void remapAllClasses(ZipFile file, ASMRemapper remapper, RemapperFormat format) throws IOException {
		Enumeration<? extends ZipEntry> entries = file.entries();
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
			String className = entry.getName().replace('/', '.');
			className = className.substring(0, className.length() - 6);
//			reader.accept(new ClassRemapper(writer, remapper) {
//				// That is a bug of ASM Library...?
//				@Override
//				public void visitInnerClass(String name, String outerName, String innerName, int access) {
//					String shouldName = remapper.mapType(name);
//					super.visitInnerClass(shouldName, outerName == null ? null : remapper.mapType(outerName),
//							shouldName.substring(shouldName.lastIndexOf('$') + 1), access);
//				}
//			}, 0);
			reader.accept(new ClassRemapper(writer, remapper), 0);
			byte[] array = writer.toByteArray();
			if (detail)
				ProgramMain.logger.info("Remapping class: " + className + " (Length:" + array.length + ")");
			write(format.remaps.get(className).mapName().replace('.', '/') + ".class", array);
		}
		ProgramMain.logger.info("Remapped all classes");
	}

	private void recreateManifest() throws IOException {
		write("META-INF/MANIFEST.MF",
				"Manifest-Version: 1.0\r\nMain-Class: net.minecraft.client.main.Main\r\n".getBytes());
		ProgramMain.logger.info("Recreated manifest");
	}

	private Map<String, ByteArrayOutputStream> buffers = new HashMap<>();

	private void write(String file, byte[] in) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		IOUtils.write(in, os);
		os.close();
		buffers.put(file, os);
	}

	private void removeModCheck() throws IOException {
		doHacks("net.minecraft.client.Minecraft", reader -> {
			ClassWriter writer = new ClassWriter(0);
			reader.accept(new RemoveModCheck(writer), 0);
			return writer;
		});
		ProgramMain.logger.info("Removed modding check");
	}

	private void hackTheName() throws IOException {
		doHacks("net.minecraft.client.gui.screens.TitleScreen", reader -> {
			ClassWriter writer = new ClassWriter(0);
			reader.accept(new ModifyTitleScreen(writer), 0);
			return writer;
		});
		ProgramMain.logger.info("Modified Title Screen");
	}

	private void changeBrand() throws IOException {
		Function<ClassReader, ClassWriter> func = reader -> {
			ClassWriter writer = new ClassStringReplacer("vanilla", "remapped");
			reader.accept(writer, 0);
			return writer;
		};
		doHacks("net.minecraft.server.MinecraftServer", func);
		doHacks("net.minecraft.client.ClientBrandRetriever", func);
		ProgramMain.logger.info("Modified Brand Tag");
	}

	private void doHacks(String className, Function<ClassReader, ClassWriter> func) throws IOException {
		String name = className.replace('.', '/') + ".class";
		ByteArrayOutputStream baos = buffers.remove(name);
		ClassReader reader = new ClassReader(baos.toByteArray());
		ClassWriter writer = func.apply(reader);
		write(name, writer.toByteArray());
	}

	public void runPack(String dest) throws IOException, InterruptedException {
		ProgramMain.logger.info("Packing into JAR...");
		File destf = new File(dest);
		if (!destf.exists())
			destf.createNewFile();
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destf));
		for (String entry : buffers.keySet()) {
			ZipEntry zipEntry = new ZipEntry(entry);
			zos.putNextEntry(zipEntry);
			zos.write(buffers.get(entry).toByteArray());
		}
		zos.close();
		ProgramMain.logger.info("Minecraft has been remapped successfully.");
	}

	public String getNowFile() {
		return nowFile;
	}

	public double getProgress() {
		return dealed / all;
	}
}
