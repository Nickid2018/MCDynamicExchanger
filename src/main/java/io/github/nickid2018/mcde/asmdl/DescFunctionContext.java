package io.github.nickid2018.mcde.asmdl;

import org.objectweb.asm.Label;

import java.util.List;
import java.util.Map;

public record DescFunctionContext(DescBlock environment,
                                  String[] args,
                                  Object visitor,
                                  Map<String, Label> labelMap,
                                  List<Object> additional,
                                  Object blockReturns) {
}
