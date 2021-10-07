package io.github.nickid2018.mcremap;

import io.github.nickid2018.I18N;
import io.github.nickid2018.ProgramMain;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.util.ClassUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

    private void addPlainClasses(ZipFile file, RemapperFormat format) throws IOException {
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
                ClassNode node = new ClassNode(Opcodes.ASM9);
                reader.accept(node, 0);
                clazz = new RemapClass(className, className, format);
                format.remaps.put(className, clazz);
                for (MethodNode mno : node.methods) {
                    MethodNode mn = mno;
                    clazz.methodMappings.put(mn.name + mn.desc, mn.name);
                }
                for (FieldNode flo : node.fields) {
                    FieldNode fl = flo;
                    clazz.fieldMappings.put(fl.name, fl.name);
                }
                if (detail)
                    ProgramMain.logger.info(I18N.getText("remap.genmap.addclasses.processing", className));
            }
        }
        ProgramMain.logger.formattedInfo("remap.genmap.addclasses.over");
    }

    private void generateExtendTree(ZipFile file, RemapperFormat format) throws IOException {
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
                ProgramMain.logger.info(I18N.getText("remap.genmap.geninherit.processing", reader.getClassName()));
        }
        ProgramMain.logger.formattedInfo("remap.genmap.geninherit.over");
    }

    private void doOutputRemap(InputStream mf, String path, RemapperFormat format) throws IOException {
        RemapClassSerializeHelper helper = RemapClassSerializeHelper.getInstance();
        helper.doSerialize(format.remaps, path, mf);
    }

    private void doOutputRevRemap(InputStream mf, String path, RemapperFormat format) throws IOException {
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
