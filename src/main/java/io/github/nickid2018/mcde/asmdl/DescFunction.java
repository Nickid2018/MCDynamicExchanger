package io.github.nickid2018.mcde.asmdl;

public abstract class DescFunction<T> {

    protected final String name;

    public DescFunction(String name) {
        this.name = name;
    }

    public abstract <F> T process(DescFunctionContext<F> context) throws ASMDLSyntaxException;

    public String name() {
        return name;
    }
}
