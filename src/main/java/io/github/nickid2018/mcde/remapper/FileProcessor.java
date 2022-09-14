package io.github.nickid2018.mcde.remapper;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileProcessor {

    public static void process(ZipFile file, File mapping, File output) throws IOException {
        MojangFormatRemapper remapper = new MojangFormatRemapper(new FileInputStream(mapping));
        addPlainClasses(file, remapper);
        generateInheritTree(file, remapper);
        runPack(output, remapAllClasses(file, remapper.getRemapper(), remapper));
    }

    public static void addPlainClasses(ZipFile file, MojangFormatRemapper format) throws IOException {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.getName().endsWith(".class"))
                continue;
            ClassReader reader = new ClassReader(IOUtils.toByteArray(file.getInputStream(entry)));
            String className = ASMRemapper.toBinaryName(reader.getClassName());
            RemapClass clazz = format.remaps.get(className);
            if (clazz == null) {
                ClassNode node = new ClassNode(Opcodes.ASM9);
                reader.accept(node, 0);
                clazz = new RemapClass(className, className);
                format.remaps.put(className, clazz);
                for (MethodNode mno : node.methods)
                    clazz.methodMappings.put(mno.name + mno.desc, mno.name);
                for (FieldNode flo : node.fields)
                    clazz.fieldMappings.put(flo.name, flo.name);
            }
        }
    }

    public static void generateInheritTree(ZipFile file, MojangFormatRemapper remapper) throws IOException {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.getName().endsWith(".class"))
                continue;
            ClassReader reader = new ClassReader(IOUtils.toByteArray(file.getInputStream(entry)));
            RemapClass clazz = remapper.remaps.get(ASMRemapper.toBinaryName(reader.getClassName()));
            clazz.superClasses.add(remapper.remaps.get(ASMRemapper.toBinaryName(reader.getSuperName())));
            for (String name : reader.getInterfaces())
                clazz.superClasses.add(remapper.remaps.get(ASMRemapper.toBinaryName(name)));
        }
    }

    public static Map<String, byte[]> remapAllClasses(ZipFile file, ASMRemapper remapper, MojangFormatRemapper format) throws IOException {
        Map<String, byte[]> remappedData = new HashMap<>();

        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            String nowFile;
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory() || entry.getName().startsWith("META-INF"))
                continue;
            byte[] bytes = IOUtils.toByteArray(file.getInputStream(entry));
            if (!(nowFile = entry.getName()).endsWith(".class")) {
                remappedData.put(nowFile, bytes);
                continue;
            }
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(0);
            String className = ASMRemapper.toBinaryName(entry.getName());
            className = className.substring(0, className.length() - 6);
            reader.accept(new ClassRemapperFix(writer, remapper), 0);
            remappedData.put(ASMRemapper.toInternalName(format.remaps.get(className).mapName()) + ".class",
                    writer.toByteArray());
        }

        remappedData.put("META-INF/MANIFEST.MF",
                "Manifest-Version: 1.0\r\nMain-Class: net.minecraft.client.main.Main".getBytes());

        return remappedData;
    }

    public static void runPack(File dest, Map<String, byte[]> map) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(dest.toPath()));
        for (String entry : map.keySet()) {
            ZipEntry zipEntry = new ZipEntry(entry);
            zos.putNextEntry(zipEntry);
            zos.write(map.get(entry));
        }
        zos.close();
    }
}
