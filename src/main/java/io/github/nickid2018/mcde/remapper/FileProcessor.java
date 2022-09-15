package io.github.nickid2018.mcde.remapper;

import io.github.nickid2018.mcde.format.MappingClassData;
import io.github.nickid2018.mcde.format.MappingFormat;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileProcessor {

    public static void processServer(ZipFile file, MappingFormat remapper, File output) throws Exception {
        String versionData = IOUtils.toString(
                file.getInputStream(file.getEntry("META-INF/versions.list")), StandardCharsets.UTF_8);
        String[] extractData = versionData.split("\t", 3);
        File tempZip = new File("temp-server.jar");
        IOUtils.copy(file.getInputStream(file.getEntry("META-INF/versions/" + extractData[2])),
                new FileOutputStream(tempZip));
        checkIntegrity(tempZip.toPath(), extractData[0]);
        try (ZipFile server = new ZipFile(tempZip)) {
            process(server, remapper, output);
        }
        tempZip.delete();
    }

    private static void checkIntegrity(Path file, String expectedHash) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream output = Files.newInputStream(file)) {
            output.transferTo(new DigestOutputStream(OutputStream.nullOutputStream(), digest));
            String actualHash = byteToHex(digest.digest());
            if (!actualHash.equalsIgnoreCase(expectedHash))
                throw new IOException("Expected file %s to have hash %s, but got %s".formatted(file, expectedHash, actualHash));
        }
    }

    private static String byteToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; ++i) {
            byte b = bytes[i];
            result.append(Character.forDigit(b >> 4 & 15, 16));
            result.append(Character.forDigit(b & 15, 16));
        }
        return result.toString();
    }

    public static void process(ZipFile file, MappingFormat remapper, File output) throws IOException {
        addPlainClasses(file, remapper);
        generateInheritTree(file, remapper);
        runPack(output, remapAllClasses(file, remapper.getToNamedMapper(), remapper));
    }

    public static void addPlainClasses(ZipFile file, MappingFormat format) throws IOException {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.getName().endsWith(".class"))
                continue;
            ClassReader reader = new ClassReader(IOUtils.toByteArray(file.getInputStream(entry)));
            String className = ASMRemapper.toBinaryName(reader.getClassName());
            MappingClassData clazz = format.getToNamedClass(className);
            if (clazz == null) {
                ClassNode node = new ClassNode(Opcodes.ASM9);
                reader.accept(node, 0);
                clazz = new MappingClassData(className, className);
                format.addRemapClass(className, clazz);
                for (MethodNode mno : node.methods)
                    clazz.methodMappings.put(mno.name + mno.desc, mno.name);
                for (FieldNode flo : node.fields)
                    clazz.fieldMappings.put(flo.name + "+" + flo.desc, flo.name);
            }
        }
    }

    public static void generateInheritTree(ZipFile file, MappingFormat format) throws IOException {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.getName().endsWith(".class"))
                continue;
            ClassReader reader = new ClassReader(IOUtils.toByteArray(file.getInputStream(entry)));
            MappingClassData clazz = format.getToNamedClass(ASMRemapper.toBinaryName(reader.getClassName()));
            clazz.superClasses.add(format.getToNamedClass(ASMRemapper.toBinaryName(reader.getSuperName())));
            for (String name : reader.getInterfaces())
                clazz.superClasses.add(format.getToNamedClass(ASMRemapper.toBinaryName(name)));
        }
        format.createToSourceMapper();
    }

    public static Map<String, byte[]> remapAllClasses(ZipFile file, ASMRemapper remapper, MappingFormat format) throws IOException {
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
            remappedData.put(ASMRemapper.toInternalName(format.getToNamedClass(className).mapName()) + ".class",
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
