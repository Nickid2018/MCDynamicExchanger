package com.github.nickid2018.decompile.cfr;

import org.objectweb.asm.ClassReader;
import org.apache.commons.io.IOUtils;
import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipFile;

public class DecompileClassFileSource implements ClassFileSource {

    private final ZipFile file;

    public DecompileClassFileSource(ZipFile file) {
        this.file = file;
    }

    @Override
    public void informAnalysisRelativePathDetail(String usePath, String classFilePath) {
    }

    @Override
    public Collection<String> addJar(String jarPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPossiblyRenamedPath(String path) {
        return path;
    }

    @Override
    public Pair<byte[], String> getClassFileContent(String path) throws IOException {
        byte[] bytes = IOUtils.toByteArray(file.getInputStream(file.getEntry(path)));
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(0);
        reader.accept(new OptiClassVisitor(writer), 0);
        return Pair.make(writer.toByteArray(), path);
    }

}
