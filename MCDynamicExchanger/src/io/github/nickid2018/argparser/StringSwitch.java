package io.github.nickid2018.argparser;

public class StringSwitch extends CommandSwitch {

    private final String name;
    private String value;

    public StringSwitch(String name) {
        this.name = name;
    }

    private StringSwitch(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public final boolean isThisSwitch(String switchName) {
        return true;
    }

    @Override
    public String getSwitchName(String name) {
        return this.name;
    }

    @Override
    public final boolean isOptional() {
        return false;
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
    public CommandSwitch getSwitchInstance(String name) {
        return new StringSwitch(this.name, name);
    }

    @Override
    public String toString() {
        return value;
    }
}
