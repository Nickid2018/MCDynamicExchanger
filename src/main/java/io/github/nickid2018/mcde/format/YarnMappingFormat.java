package io.github.nickid2018.mcde.format;

import io.github.nickid2018.mcde.remapper.ASMRemapper;
import io.github.nickid2018.mcde.util.ClassUtils;
import io.github.nickid2018.mcde.util.I18N;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class YarnMappingFormat extends MappingFormat {

    public YarnMappingFormat(InputStream stream) throws IOException {
        createToNameMapper(new BufferedReader(new InputStreamReader(stream)));
    }

    private void createToNameMapper(BufferedReader reader) throws IOException {
        // Line 1: tiny\t2\t0\tofficial\tintermediary\tnamed
        String head = reader.readLine();
        if (!head.equalsIgnoreCase("tiny\t2\t0\tofficial\tintermediary\tnamed"))
            throw new IOException(I18N.getTranslation("error.mapping.format"));
        String line;
        MappingClassData mappingClassData = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("\t") && line.trim().startsWith("c"))
                continue;
            String[] args = line.trim().split("[\t ]");
            if (args[0].equalsIgnoreCase("c")) {
                // Class Line: c official intermediary named
                String sourceName = ClassUtils.toBinaryName(args[1]);
                mappingClassData = new MappingClassData(sourceName, ClassUtils.toBinaryName(args[3]));
                remaps.put(sourceName, mappingClassData);
            } else if (args[0].equalsIgnoreCase("f"))
                // Field line: f desc official intermediary named
                mappingClassData.fieldMappings.put(args[2] + "+" + args[1], args[4]);
            else if (args[0].equalsIgnoreCase("m"))
                // Method Line: m desc official intermediary named
                mappingClassData.methodMappings.put(args[2] + args[1], args[4]);
        }
        toNamedMapper = new ASMRemapper(remaps);
    }
}
