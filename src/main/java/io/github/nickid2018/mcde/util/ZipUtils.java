package io.github.nickid2018.mcde.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void zipFolders(String srcFolder, String destZipFile) throws Exception {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destZipFile));
        for (File dirs : Objects.requireNonNull(new File(srcFolder).listFiles(File::isDirectory)))
            addFolderToZip("", dirs.getAbsolutePath(), zip);
        zip.flush();
        zip.close();
    }

    private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
        File folder = new File(srcFolder);

        for (String fileName : Objects.requireNonNull(folder.list())) {
            if (path.equals(""))
                addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
            else
                addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
        }
    }

    private static void addFileToZip(String name, String filePath, ZipOutputStream zip) throws Exception {
        File file = new File(filePath);
        if (file.isDirectory())
            addFolderToZip(name, filePath, zip);
        else {
            FileInputStream in = new FileInputStream(filePath);
            zip.putNextEntry(new ZipEntry(name + "/" + file.getName()));
            IOUtils.copy(in, zip);
            in.close();
        }
    }
}
