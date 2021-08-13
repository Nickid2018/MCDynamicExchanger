package com.github.nickid2018.mcremap;

import com.github.nickid2018.I18N;
import com.github.nickid2018.ProgramMain;
import com.github.nickid2018.argparser.CommandResult;
import com.github.nickid2018.mcremap.optimize.ModifyTitleScreen;
import com.github.nickid2018.mcremap.optimize.OptimizedClassRemapper;
import com.github.nickid2018.mcremap.optimize.OptimizedMethodRemapper;
import com.github.nickid2018.util.ClassStringReplacer;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileRemapper {

    private final Map<String, ByteArrayOutputStream> buffers = new HashMap<>();
    private String nowFile;
    private double dealed = 0;
    private double all;
    private boolean detail;

    public void remapAll(CommandResult result, RemapperFormat format) throws Exception {
        detail = result.containsSwitch("-D");
        if (result.containsSwitch("-Nl"))
            OptimizedMethodRemapper.setNoLineNumbers();
        ZipFile file = new ZipFile(new File(result.getSwitch("mc_file").toString()));
        all = file.size();
        // Remap
        remapAllClasses(file, new ASMRemapper(format), format);
        file.close();
        // MANIFEST.MF
        recreateManifest();
        if (!result.containsSwitch("-Nh")) {
            // Change Brand
            changeBrand();
            // Add Remapped Mark
            hackTheName();
        }
        runPack(result.getStringOrDefault("--output", "remapped.jar"));
    }

    private void remapAllClasses(ZipFile file, ASMRemapper remapper, RemapperFormat format) throws IOException {
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
            reader.accept(new OptimizedClassRemapper(writer, remapper), 0);
            byte[] array = writer.toByteArray();
            if (detail)
                ProgramMain.logger.info(I18N.getText("remap.classremap.processing", className, array.length));
            write(format.remaps.get(className).mapName().replace('.', '/') + ".class", array);
        }
        ProgramMain.logger.formattedInfo("remap.classremap.over");
    }

    private void recreateManifest() throws IOException {
        write("META-INF/MANIFEST.MF",
                "Manifest-Version: 1.0\r\nMain-Class: net.minecraft.client.main.Main\r\n".getBytes());
        ProgramMain.logger.formattedInfo("remap.mod.manifest");
    }

    private void write(String file, byte[] in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        IOUtils.write(in, os);
        os.close();
        buffers.put(file, os);
    }

    private void hackTheName() throws IOException {
        doHacks("net.minecraft.client.gui.screens.TitleScreen", reader -> {
            ClassWriter writer = new ClassWriter(0);
            reader.accept(new ModifyTitleScreen(writer), 0);
            return writer;
        });
        ProgramMain.logger.formattedInfo("remap.mod.title");
    }

    private void changeBrand() throws IOException {
        Function<ClassReader, ClassWriter> func = reader -> {
            ClassWriter writer = new ClassStringReplacer("vanilla", "remapped");
            reader.accept(writer, 0);
            return writer;
        };
        doHacks("net.minecraft.server.MinecraftServer", func);
        doHacks("net.minecraft.client.ClientBrandRetriever", func);
        ProgramMain.logger.formattedInfo("remap.mod.brand");
    }

    private void doHacks(String className, Function<ClassReader, ClassWriter> func) throws IOException {
        String name = className.replace('.', '/') + ".class";
        ByteArrayOutputStream baos = buffers.remove(name);
        ClassReader reader = new ClassReader(baos.toByteArray());
        ClassWriter writer = func.apply(reader);
        write(name, writer.toByteArray());
    }

    public void runPack(String dest) throws IOException {
        ProgramMain.logger.formattedInfo("remap.pack");
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
        ProgramMain.logger.formattedInfo("remap.over");
    }

    public String getNowFile() {
        return nowFile;
    }

    public double getProgress() {
        return dealed / all;
    }
}
