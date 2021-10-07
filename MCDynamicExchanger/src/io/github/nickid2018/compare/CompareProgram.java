package io.github.nickid2018.compare;

import io.github.nickid2018.I18N;
import io.github.nickid2018.SortedConsoleLogger;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.util.AddClassPath;
import io.github.nickid2018.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static io.github.nickid2018.ProgramMain.logger;

public class CompareProgram {

    public final List<String> files = new ArrayList<>();
    public final Map<String, InputStream> oldVersionEntries = new HashMap<>();
    private final ZipFile oldV;
    private final ZipFile newV;
    private final boolean excludeRes;
    public Enumeration<? extends ZipEntry> newVersionEntries;

    public CompareProgram(String oldVersion, String newVersion, boolean excludeRes) throws IOException {
        oldV = new ZipFile(oldVersion);
        newV = new ZipFile(newVersion);
        newVersionEntries = newV.entries();
        this.excludeRes = excludeRes;
        initOldVersion();
    }

    public static void compareSimple(CommandResult res) {
        logger = new SortedConsoleLogger();
        if (!ClassUtils.isClassExists("org.apache.commons.io.IOUtils")
                && !AddClassPath.tryToLoadMCLibrary("commons-io/commons-io")) {
            logger.formattedInfo("error.libraries.io");
            logger.flush();
            return;
        }
        try {
            CompareProgram program = new CompareProgram(res.getSwitch("old_version").toString(),
                    res.getSwitch("new_version").toString(), !res.containsSwitch("-Rs"));
            while (program.hasNext()) {
                CompareResult result = program.next();
                if (result == null)
                    continue;
                if (result.type != CompareResultType.NONE)
                    logger.info(result.getMessage()
                            + (res.containsSwitch("-D") ? "[SHA-256: " + result.oldSHA256 + " -> " + result.newSHA256 + "]"
                            : ""));
            }
            program.close();
        } catch (Throwable e) {
            logger.error(I18N.getText("error.unknown"), e);
        }
        logger.flush();
    }

    public void initOldVersion() throws IOException {
        Enumeration<? extends ZipEntry> oldEntries = oldV.entries();
        while (oldEntries.hasMoreElements()) {
            ZipEntry entry = oldEntries.nextElement();
            String name = entry.getName();
            boolean isClass = name.endsWith(".class");
            if (!isClass && excludeRes)
                continue;
            name = (isClass ? name.replace('/', '.') : name).substring(0, name.length() - (isClass ? 6 : 0));
            oldVersionEntries.put(name, oldV.getInputStream(entry));
            files.add(name);
        }
    }

    public boolean hasNext() {
        return !oldVersionEntries.isEmpty() || newVersionEntries.hasMoreElements();
    }

    public CompareResult next() throws IOException {
        while (newVersionEntries.hasMoreElements()) {
            ZipEntry entry = newVersionEntries.nextElement();
            String name = entry.getName();
            boolean isClass = name.endsWith(".class");
            if (isClass || !excludeRes) {
                name = (isClass ? name.replace('/', '.') : name).substring(0, name.length() - (isClass ? 6 : 0));
                files.remove(name);
                return new CompareResult(name, oldVersionEntries.remove(name), newV.getInputStream(entry));
            }
        }
        if (!oldVersionEntries.isEmpty()) {
            String name = files.remove(0);
            return new CompareResult(name, oldVersionEntries.remove(name), null);
        }
        return null;
    }

    public void close() throws IOException {
        oldV.close();
        newV.close();
    }
}
