package com.github.nickid2018.argparser;

import java.util.*;

public class UnorderSwitchTable extends CommandSwitch {

	private Map<String, CommandSwitch> switches;

	public UnorderSwitchTable() {
		switches = new HashMap<>();
	}

	public void addSwitch(String name, CommandSwitch _switch) {
		if (!_switch.isOptional() || _switch.isRepeatable())
			throw new IllegalArgumentException("The unorder switch is illegal!");
		switches.put(name, _switch);
	}

	public void addLiteral(LiteralSwitch _switch) {
		addSwitch(_switch.getName(), _switch);
	}

	@Override
	public final boolean isThisSwitch(String switchName) {
		return switches.containsKey(switchName);
	}

	@Override
	public final String getSwitchName(String name) {
		return name;
	}

	@Override
	public final boolean isOptional() {
		return true;
	}

	@Override
	public final boolean isRepeatable() {
		return true;
	}

	@Override
	public final int requireArguments() {
		return 0;
	}

	@Override
	public final void nextArgument(int index, String value) {
	}

	@Override
	public CommandSwitch getSwitchInstance(String name) {
		return switches.get(name).getSwitchInstance(name);
	}

}
