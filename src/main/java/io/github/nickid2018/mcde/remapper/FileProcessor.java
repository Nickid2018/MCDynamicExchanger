package io.github.nickid2018.mcde.remapper;

import io.github.nickid2018.mcde.format.MappingClassData;
import io.github.nickid2018.mcde.format.MappingFormat;
import io.github.nickid2018.mcde.util.ClassUtils;
import io.github.nickid2018.mcde.util.I18N;
import io.github.nickid2018.mcde.util.LogUtils;
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
        LogUtils.log("process.remap.server");
        String versionData = IOUtils.toString(
                file.getInputStream(file.getEntry("META-INF/versions.list")), StandardCharsets.UTF_8);
        String[] extractData = versionData.split("\t", 3);
        File tempZip = new File("temp-server.jar");
        IOUtils.copy(file.getInputStream(file.getEntry("META-INF/versions/" + extractData[2])),
                new FileOutputStream(tempZip));
        checkIntegrity(tempZip.toPath(), extractData[0]);
        LogUtils.log("process.remap.server.done");
        try (ZipFile server = new ZipFile(tempZip)) {
            process(server, remapper, output, true);
        }
        tempZip.delete();
    }

    private static void checkIntegrity(Path file, String expectedHash) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream output = Files.newInputStream(file)) {
            output.transferTo(new DigestOutputStream(OutputStream.nullOutputStream(), digest));
            String actualHash = byteToHex(digest.digest());
            if (!actualHash.equalsIgnoreCase(expectedHash))
                throw new IOException(I18N.getTranslation("error.remap.serverhash", expectedHash, actualHash));
        }
    }

    private static String byteToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            result.append(Character.forDigit(b >> 4 & 15, 16));
            result.append(Character.forDigit(b & 15, 16));
        }
        return result.toString();
    }

    public static void process(ZipFile file, MappingFormat remapper, File output, boolean server) throws IOException {
        LogUtils.log("process.remap.inheritance");
        addPlainClasses(file, remapper);
        generateInheritTree(file, remapper);
        LogUtils.log("process.remap.inheritance.done");
        LogUtils.log("process.remap.output");
        runPack(output, remapAllClasses(file, remapper.getToNamedMapper(), remapper, server));
        LogUtils.log("process.remap.success");
    }

    public static void addPlainClasses(ZipFile file, MappingFormat format) throws IOException {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.getName().endsWith(".class"))
                continue;
            ClassReader reader = new ClassReader(IOUtils.toByteArray(file.getInputStream(entry)));
            String className = ClassUtils.toBinaryName(reader.getClassName());
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
            MappingClassData clazz = format.getToNamedClass(ClassUtils.toBinaryName(reader.getClassName()));
            clazz.superClasses.add(format.getToNamedClass(ClassUtils.toBinaryName(reader.getSuperName())));
            for (String name : reader.getInterfaces())
                clazz.superClasses.add(format.getToNamedClass(ClassUtils.toBinaryName(name)));
        }
    }

    public static Map<String, byte[]> remapAllClasses(ZipFile file, ASMRemapper remapper, MappingFormat format, boolean server)
            throws IOException {
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
            String className = ClassUtils.toBinaryName(entry.getName());
            className = className.substring(0, className.length() - 6);
            reader.accept(new ClassRemapperFix(writer, remapper), 0);
            remappedData.put(ClassUtils.toInternalName(format.getToNamedClass(className).mapName()) + ".class",
                    writer.toByteArray());
        }

        remappedData.put("META-INF/MANIFEST.MF",
                ("Manifest-Version: 1.0\r\nMain-Class: " +
                        (server ? "net.minecraft.server.Main" : "net.minecraft.client.main.Main")).getBytes());

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
