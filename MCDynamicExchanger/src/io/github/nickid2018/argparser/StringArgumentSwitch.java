package io.github.nickid2018.argparser;

public class StringArgumentSwitch extends CommandSwitch {

    private final String name;
    private String value;

    public StringArgumentSwitch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public StringArgumentSwitch setValue(String str) {
        value = str;
        return this;
    }

    @Override
    public boolean isThisSwitch(String switchName) {
        return name.equals(switchName);
    }

    @Override
    public String getSwitchName(String name) {
        return name;
    }

    @Override
    public final boolean isOptional() {
        return true;
    }

    @Override
    public final boolean isRepeatable() {
        return false;
    }

    @Override
    public final int requireArguments() {
        return 1;
    }

    @Override
    public final void nextArgument(int index, String value) {
        this.value = value;
    }

    @Override
    public CommandSwitch getSwitchInstance(String name) {
        return new StringArgumentSwitch(name);
    }

    @Override
    public String toString() {
        return value;
    }
}
