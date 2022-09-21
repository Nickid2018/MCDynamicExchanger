package io.github.nickid2018.mcde.asmdl;

import org.objectweb.asm.Label;

import java.util.Map;

public record DescFunctionContext<T>(DescBlock<T> environment, String[] args, T visitor, Map<String, Label> labelMap) {
}
