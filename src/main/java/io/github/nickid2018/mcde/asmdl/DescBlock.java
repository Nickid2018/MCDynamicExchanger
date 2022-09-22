package io.github.nickid2018.mcde.asmdl;

public abstract class DescBlock extends DescFunction {

    public DescBlock(String name) {
        super(name);
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
    }


    public abstract Object processStart(DescFunctionContext context) throws ASMDLSyntaxException;

    public abstract Object processEnd(DescFunctionContext context) throws ASMDLSyntaxException;
}
