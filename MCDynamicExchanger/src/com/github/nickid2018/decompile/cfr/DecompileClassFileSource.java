package com.github.nickid2018.decompile.cfr;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.benf.cfr.reader.api.*;
import org.apache.commons.io.*;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.*;

public class DecompileClassFileSource implements ClassFileSource {

	private ZipFile file;

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
		return Pair.make(bytes, path);
	}

}
