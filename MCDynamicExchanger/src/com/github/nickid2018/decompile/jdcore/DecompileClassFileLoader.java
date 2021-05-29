package com.github.nickid2018.decompile.jdcore;

import java.io.*;
import java.util.zip.*;
import org.jd.core.v1.api.loader.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;

public class DecompileClassFileLoader implements Loader {

	private ZipFile file;

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
