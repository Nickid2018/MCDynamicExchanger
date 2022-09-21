package io.github.nickid2018.mcde.asmdl;

public abstract class DescBlock<T> extends DescFunction<T> {

    public DescBlock(String name) {
        super(name);
    }

    public abstract <F> void processEnd(DescFunctionContext<F> context) throws ASMDLSyntaxException;
}
