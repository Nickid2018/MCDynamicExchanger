package io.github.nickid2018.mcde.remapper;

import java.util.HashMap;
import java.util.Map;

public abstract class FormatRemapper {

    public final Map<String, RemapClass> remaps = new HashMap<>();
    public final Map<String, String> revClass = new HashMap<>();
    protected ASMRemapper remapper;

    public ASMRemapper getRemapper() {
        return remapper;
    }
}
