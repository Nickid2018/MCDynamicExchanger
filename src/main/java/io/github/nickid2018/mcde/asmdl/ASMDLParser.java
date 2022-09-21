package io.github.nickid2018.mcde.asmdl;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class ASMDLParser {

    private final String[] lines;

    public ASMDLParser(String data) {
        lines = data.split("\n");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public byte[] toClass() throws ASMDLSyntaxException {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        Stack<DescBlock<?>> environmentStack = new Stack<>();
        Stack<Object> visitorStack = new Stack<>();
        Stack<Map<String, Label>> labelMap = new Stack<>();
        visitorStack.push(writer);

        for (String line : lines) {
            String lineTrim = line.trim();
            if (lineTrim.isEmpty() || lineTrim.startsWith("#"))
                continue;

            String[] args = lineTrim.split("#")[0].trim().split(" ");
            if (args.length == 0)
                continue;

            String[] argsPass = new String[args.length - 1];
            System.arraycopy(args, 1, argsPass, 0, args.length - 1);

            String name = args[0];

            if (name.equals("}") && argsPass.length == 0) {
                if (environmentStack.isEmpty())
                    throw new ASMDLSyntaxException("unexpected }");
                DescBlock<?> lastEnv = environmentStack.pop();
                labelMap.pop();
                lastEnv.processEnd(new DescFunctionContext(environmentStack.isEmpty() ? null : environmentStack.peek(),
                        new String[0], visitorStack.pop(), labelMap.isEmpty() ? null : labelMap.peek()));
                continue;
            }

            DescFunction<?> function = DescFunctions.FUNCTIONS.get(name);
            DescBlock env = environmentStack.isEmpty() ? null : environmentStack.peek();
            Map<String, Label> labels = labelMap.isEmpty() ? null : labelMap.peek();
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

                Object obj = block.process(new DescFunctionContext(env, argsPass, visitorStack.peek(),
                        labelMapNow));
                if (needClose) {
                    environmentStack.push(block);
                    visitorStack.push(obj);
                } else
                    block.processEnd(new DescFunctionContext(env, argsPass, obj, labels));
            } else
                function.process(new DescFunctionContext(env, argsPass, visitorStack.peek(), labels));
        }

        if (!environmentStack.isEmpty())
            throw new ASMDLSyntaxException("unexpected EOF");
        return writer.toByteArray();
    }

    public static void main(String[] args) {
        String data = """
                class 17 public final Test implements java/lang/Runnable {
                    field public final name Ljava/lang/String;
                    
                    method public <init> (Ljava/lang/String;)V {
                        aload 0
                        invokespecial java/lang/Object.<init>()V
                        aload 0
                        aload 1
                        putfield Test.name Ljava/lang/String;
                        return
                    }
                    
                    method public run ()V {
                        aload 0
                        getfield Test.name Ljava/lang/String;
                        invokestatic Test.test(Ljava/lang/String;)Ljava/lang/String;
                        astore 1
                        iconst_0
                        istore 2
                        label loop {
                            iload 2
                            bipush 100
                            if_icmpge end
                            getstatic java/lang/System.out Ljava/io/PrintStream;
                            aload 1
                            invokevirtual java/io/PrintStream.println(Ljava/lang/String;)V
                            iinc 2 1
                            goto loop
                        }
                        label end {
                            return
                        }
                    }
                    
                    method static test (Ljava/lang/String;)Ljava/lang/String; {
                        aload 0
                        iconst_4
                        invokevirtual java/lang/String.repeat(I)Ljava/lang/String;
                        astore 1
                        invokestatic java/lang/System.currentTimeMillis()J
                        l2i
                        iconst_2
                        irem
                        iconst_1
                        if_icmpne end
                        label do_trim {
                            aload 1
                            invokevirtual java/lang/String.toUpperCase()Ljava/lang/String;
                            astore 1
                        }
                        label end {
                            aload 1
                            areturn
                        }
                    }
                }
                """;

        ASMDLParser parser = new ASMDLParser(data);
        try {
            byte[] bytes = parser.toClass();
            ClassLoader loader = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) {
                    return defineClass(name, bytes, 0, bytes.length);
                }
            };
            IOUtils.write(bytes, new FileOutputStream("D:\\Test.class"));
            Class<?> cls = loader.loadClass("Test");
            Runnable r = (Runnable) cls.getConstructor(String.class).newInstance("   Hello World!   ");
            r.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
