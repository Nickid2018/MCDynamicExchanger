package io.github.nickid2018.mcde.remapper;

import io.github.nickid2018.mcde.util.ClassUtils;

import java.io.*;

public class MojangFormatRemapper extends FormatRemapper {

    public MojangFormatRemapper(InputStream stream) throws IOException {
        super();
        ByteArrayInputStream resetStream = new ByteArrayInputStream(stream.readAllBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(resetStream));
        resetStream.mark(0);
        readClassTokens(reader);
        resetStream.reset();
        readComponents(reader);
        remapper = new ASMRemapper(this);
    }

    private void readClassTokens(BufferedReader reader) throws IOException {
        String nowClass;
        String toClass;
        String nowStr;
        while ((nowStr = reader.readLine()) != null) {
            if (nowStr.startsWith("#"))
                continue;
            String now = nowStr.trim();
            if (!nowStr.startsWith(" ")) {
                // Class
                now = now.substring(0, now.length() - 1);
                String[] splits = now.split(" -> ");
                nowClass = splits[0];
                toClass = splits[1];
                remaps.put(toClass, new RemapClass(toClass, nowClass));
                revClass.put(nowClass, toClass);
            }
        }
    }

    private void readComponents(BufferedReader reader) throws IOException {
        String toClass;
        String nowStr;
        RemapClass nowClass = new RemapClass("" ,"");
        while ((nowStr = reader.readLine()) != null) {
            if (nowStr.startsWith("#"))
                continue;
            String now = nowStr.trim();
            if (nowStr.startsWith(" ")) {
                if (now.indexOf('(') >= 0) {
                    // Function
                    String[] ssource = now.split(":", 3);
                    String[] splits = ssource[ssource.length - 1].trim().split(" -> ");
                    StringBuilder nowTo = new StringBuilder(splits[1] + "(");
                    String[] descs = splits[0].split(" ");
                    String[] argss = descs[1].split("[()]");
                    if (argss.length == 2) {
                        String[] args = argss[1].split(",");
                        for (String a : args) {
                            nowTo.append(ClassUtils.mapSignature(a, revClass));
                        }
                    }
                    nowTo.append(")");
                    nowTo.append(ClassUtils.mapSignature(descs[0], revClass));
                    String source = nowTo.toString().trim();
                    String to = descs[1].split("\\(")[0].trim();
                    nowClass.methodMappings.put(source, to);
                } else {
                    // Field
                    String[] splits = now.trim().split(" -> ");
                    String source = splits[1];
                    String to = splits[0].split(" ")[1];
                    nowClass.fieldMappings.put(source + "+" + ClassUtils.mapSignature(splits[0].split(" ")[0], revClass), to);
                }
            } else {
                // Class
                now = now.substring(0, now.length() - 1);
                String[] splits = now.split(" -> ");
                toClass = splits[1];
                nowClass = remaps.get(toClass);
            }
        }
    }
}
