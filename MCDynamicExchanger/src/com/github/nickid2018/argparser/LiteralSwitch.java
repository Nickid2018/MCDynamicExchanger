package com.github.nickid2018.argparser;

public class LiteralSwitch extends CommandSwitch {

	private final String name;
	private final boolean optional;

	public LiteralSwitch(String name, boolean optional) {
		this.name = name;
		this.optional = optional;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isThisSwitch(String switchName) {
		return name.equals(switchName);
	}

	@Override
	public final String getSwitchName(String name) {
		return name;
	}

	@Override
	public boolean isOptional() {
		return optional;
	}

	@Override
	public final boolean isRepeatable() {
		return false;
	}

	@Override
	public final int requireArguments() {
		return 0;
	}

	@Override
	public final void nextArgument(int index, String value) {
	}

	@Override
	public final CommandSwitch getSwitchInstance(String name) {
		return this;
	}

}
