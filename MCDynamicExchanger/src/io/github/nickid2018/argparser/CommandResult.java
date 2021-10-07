package io.github.nickid2018.argparser;

import java.util.HashMap;
import java.util.Map;

public class CommandResult {

    private final Map<String, CommandSwitch> switches;

    public CommandResult() {
        switches = new HashMap<>();
    }

    public void putSwitch(String name, CommandSwitch _switch) {
        switches.put(name, _switch);
    }

    public boolean containsSwitch(String name) {
        return switches.containsKey(name);
    }

    public CommandSwitch getSwitch(String name) {
        return switches.get(name);
    }

    public String getStringOrDefault(String name, String def) {
        return switches.containsKey(name) ? switches.get(name).toString() : def;
    }

    public void merge(CommandResult result) {
        switches.putAll(result.switches);
    }
}
