package com.github.nickid2018.mcremap;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.ProgramMain;
import com.github.nickid2018.argparser.*;

public class ClassTreeGenerator {

	private String nowFile;
	private double dealed = 0;
	private double all;
	private boolean detail;

	public final void runGenerate(CommandResult result, RemapperFormat format) throws IOException {
		detail = result.containsSwitch("-D");
		ZipFile file = new ZipFile(new File(result.getSwitch("mc_file").toString()));
		all = file.size() * 2;// Run 2 times
		// Generate Class Extends Tree
		addPlainClasses(file, format);
		generateExtendTree(file, format);
		if (result.containsSwitch("--outmap"))
			doOutputRemap(file.getInputStream(file.getEntry("META-INF/MOJANGCS.SF")),
					result.getSwitch("--outmap").toString(), format);
		if (result.containsSwitch("--outrev"))
			doOutputRevRemap(file.getInputStream(file.getEntry("META-INF/MOJANGCS.SF")),
					result.getSwitch("--outrev").toString(), format);
		file.close();
	}

	private final void addPlainClasses(ZipFile file, RemapperFormat format) throws IOException {
		Enumeration<? extends ZipEntry> entries = file.entries();
		while (entries.hasMoreElements()) {
			dealed++;
			ZipEntry entry = entries.nextElement();
			if (!(nowFile = entry.getName()).endsWith(".class"))
				continue;
			ClassReader reader = new ClassReader(IOUtils.toByteArray(file.getInputStream(entry)));
			String className = ClassUtils.toBinaryName(reader.getClassName());
			RemapClass clazz = format.remaps.get(className);
			if (clazz == null) {
				ClassNode node = new ClassNode(Opcodes.ASM6);
				reader.accept(node, ClassReader.SKIP_CODE);
				clazz = new RemapClass(className, className, format);
				format.remaps.put(className, clazz);
				for (Object mno : node.methods) {
					MethodNode mn = (MethodNode) mno;
					clazz.methodMappings.put(mn.name + mn.desc, mn.name);
				}
				for (Object flo : node.fields) {
					FieldNode fl = (FieldNode) flo;
					clazz.fieldMappings.put(fl.name, fl.name);
				}
				if (detail)
					ProgramMain.logger.info("Add unobscured class: " + className);
			}
		}
		ProgramMain.logger.info("Added all unobscured classes");
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
			if (detail)
				ProgramMain.logger.info("Generate inherit tree: " + reader.getClassName());
		}
		ProgramMain.logger.info("Generated all inherit trees");
	}

	private final void doOutputRemap(InputStream mf, String path, RemapperFormat format) throws IOException {
		RemapClassSerializeHelper helper = RemapClassSerializeHelper.getInstance();
		helper.doSerialize(format.remaps, path, mf);
	}

	private final void doOutputRevRemap(InputStream mf, String path, RemapperFormat format) throws IOException {
		RemapClassSerializeHelper helper = RemapClassSerializeHelper.getInstance();
		helper.doReverseSerialize(format.remaps, path, mf);
	}

	public String getNowFile() {
		return nowFile;
	}

	public double getProgress() {
		return dealed / all;
	}
}
