package io.github.nickid2018.mcde.asmdl;

public abstract class DescFunction {

    protected final String name;

    public DescFunction(String name) {
        this.name = name;
    }

    public abstract void process(DescFunctionContext context) throws ASMDLSyntaxException;

    public String name() {
        return name;
    }
}
