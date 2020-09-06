package com.github.nickid2018.mcremap.argparser;

public abstract class CommandSwitch {

	public abstract boolean isThisSwitch(String switchName);

	public abstract String getSwitchName(String name);

	public abstract boolean isOptional();

	public abstract boolean isRepeatable();

	public abstract int requireArguments();

	public abstract void nextArgument(int index, String value);

	public abstract CommandSwitch getSwitchInstance(String name);
}
