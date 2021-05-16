package com.github.nickid2018.util;

import java.io.*;
import java.net.*;
import java.lang.reflect.*;

public class AddClassPath {

	public static final boolean addClassPath(String path) {
		return addClassPath(new File(path));
	}

	public static final boolean addClassPath(File path) {
		try {
			URLClassLoader loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
			Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addURL.setAccessible(true);
			addURL.invoke(loader, path.toURI().toURL());
			return true;
		} catch (Exception e) {
			System.err.println("Warning: cannot load library " + path);
			return false;
		}
	}

	public static final boolean addClassPathInDirs(String path) {
		return addClassPathInDirs(new File(path));
	}

	public static final boolean addClassPathInDirs(File path) {
		boolean yes = true;
		File[] files = path.listFiles();
		if (files == null)
			return false;
		for (File file : files) {
			if (!file.getName().endsWith(".jar"))
				continue;
			if (file.isDirectory())
				yes &= addClassPathInDirs(file);
			else
				yes &= addClassPath(file);
		}
		return yes;
	}

	public static final boolean tryToLoadMCLibrary(String name) {
		File library = new File("libraries/" + name);
		if (!library.isDirectory())
			return false;
		File dirLoad = new File(library, library.list()[0]);
		return addClassPathInDirs(dirLoad);
	}
}
