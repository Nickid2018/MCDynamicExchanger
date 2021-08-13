package com.github.nickid2018.decompile.jdcore;

import com.github.nickid2018.util.ClassUtils;
import org.apache.commons.io.IOUtils;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class DecompileClassFileLoader implements Loader {

    private final ZipFile file;

    public DecompileClassFileLoader(ZipFile sourceJar) {
        file = sourceJar;
    }

    @Override
    public boolean canLoad(String name) {
        return file.getEntry(ClassUtils.toInternalName(name) + ".class") != null || name.startsWith("java");
    }

    @Override
    public byte[] load(String name) throws LoaderException {
        try {
            InputStream is = file.getInputStream(file.getEntry(ClassUtils.toInternalName(name) + ".class"));
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            InputStream is = DecompileClassFileLoader.class
                    .getResourceAsStream("/" + ClassUtils.toInternalName(name) + ".class");
            try {
                return IOUtils.toByteArray(is);
            } catch (IOException e1) {
                e1.printStackTrace();
                throw new LoaderException(e);
            }
        }
    }

}
