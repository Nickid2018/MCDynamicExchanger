package io.github.nickid2018.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class AddClassPath {

    public static boolean addClassPath(String path) {
        return addClassPath(new File(path));
    }

    public static boolean addClassPath(File path) {
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

    public static void addClassPathInDirs(String path) {
        addClassPathInDirs(new File(path));
    }

    public static boolean addClassPathInDirs(File path) {
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

    public static boolean tryToLoadMCLibrary(String name) {
        File library = new File("libraries/" + name);
        if (!library.isDirectory())
            return false;
        File dirLoad = new File(library, library.list()[0]);
        return addClassPathInDirs(dirLoad);
    }
}
