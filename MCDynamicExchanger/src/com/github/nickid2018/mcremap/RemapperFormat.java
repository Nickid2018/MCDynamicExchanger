package com.github.nickid2018.mcremap;

import com.github.nickid2018.argparser.CommandResult;

import java.util.HashMap;
import java.util.Map;

public abstract class RemapperFormat {

    public final Map<String, RemapClass> remaps = new HashMap<>();
    public final Map<String, String> revClass = new HashMap<>();

    protected final boolean detail;

    public RemapperFormat(CommandResult result) {
        detail = result.containsSwitch("-D");
    }

    public abstract void processInitMap(String position) throws Exception;

    public abstract double getProcessInValue();
}
