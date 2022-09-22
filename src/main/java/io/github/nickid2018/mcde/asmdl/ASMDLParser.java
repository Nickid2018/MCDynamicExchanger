package io.github.nickid2018.mcde.asmdl;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;

import java.util.*;

public class ASMDLParser {

    private final String[] lines;

    public ASMDLParser(String data) {
        lines = data.split("\n");
    }

    public byte[] toClass() throws ASMDLSyntaxException {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        Stack<DescBlock> environmentStack = new Stack<>();
        Stack<Object> visitorStack = new Stack<>();
        Stack<Map<String, Label>> labelMap = new Stack<>();
        Stack<List<Object>> additionalStack = new Stack<>();
        visitorStack.push(writer);

        for (String line : lines) {
            String lineTrim = line.trim();
            if (lineTrim.isEmpty() || lineTrim.startsWith("#"))
                continue;

            String[] args = lineTrim.split(" ");
            if (args.length == 0)
                continue;

            String[] argsPass = new String[args.length - 1];
            System.arraycopy(args, 1, argsPass, 0, args.length - 1);

            String name = args[0];

            if (name.equals("}") && argsPass.length == 0) {
                if (environmentStack.isEmpty())
                    throw new ASMDLSyntaxException("unexpected }");
                DescBlock lastEnv = environmentStack.pop();
                labelMap.pop();
                List<Object> additionalNow = additionalStack.pop();
                Object returns = lastEnv.processEnd(new DescFunctionContext(environmentStack.isEmpty() ? null : environmentStack.peek(),
                        new String[0], visitorStack.pop(), labelMap.isEmpty() ? null : labelMap.peek(),
                        additionalNow, null));
                lastEnv.process(new DescFunctionContext(environmentStack.isEmpty() ? null : environmentStack.peek(),
                        new String[0], visitorStack.peek(), labelMap.isEmpty() ? null : labelMap.peek(),
                        additionalStack.isEmpty() ? null : additionalStack.peek(), returns));
                continue;
            }

            DescFunction function = DescFunctions.FUNCTIONS.get(name);
            DescBlock env = environmentStack.isEmpty() ? null : environmentStack.peek();
            Map<String, Label> labels = labelMap.isEmpty() ? null : labelMap.peek();
            List<Object> additional = additionalStack.isEmpty() ? null : additionalStack.peek();
            if (function == null)
                throw new ASMDLSyntaxException("Unknown function " + name);
            if (function instanceof DescBlock block) {
                boolean needClose = false;
                if (argsPass[argsPass.length - 1].equals("{")) {
                    needClose = true;
                    String[] newArgs = new String[argsPass.length - 1];
                    System.arraycopy(argsPass, 0, newArgs, 0, argsPass.length - 1);
                    argsPass = newArgs;
                }

                Map<String, Label> labelMapNow = function == DescFunctions.METHOD ? new HashMap<>() : labels;
                labelMap.push(labelMapNow);

                List<Object> additionalNow = new ArrayList<>();
                additionalStack.push(additionalNow);

                Object obj = block.processStart(new DescFunctionContext(env, argsPass, visitorStack.peek(),
                        labelMapNow, additionalNow, null));
                if (needClose) {
                    environmentStack.push(block);
                    visitorStack.push(obj);
                } else
                    block.process(new DescFunctionContext(environmentStack.isEmpty() ? null : environmentStack.peek(),
                            new String[0], visitorStack.peek(), labelMap.isEmpty() ? null : labelMap.peek(),
                            additional,
                            block.processEnd(new DescFunctionContext(env, argsPass, obj, labels, additionalNow, null))));
            } else
                function.process(new DescFunctionContext(env, argsPass, visitorStack.peek(), labels, additional, null));
        }

        if (!environmentStack.isEmpty())
            throw new ASMDLSyntaxException("unexpected EOF");
        return writer.toByteArray();
    }
}
