package com.github.nickid2018.mcremap.argparser;

import java.util.*;

public class CommandResult {

	private Map<String, CommandSwitch> switches;

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
}
