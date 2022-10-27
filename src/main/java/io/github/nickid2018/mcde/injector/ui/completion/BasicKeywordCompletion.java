package io.github.nickid2018.mcde.injector.ui.completion;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

import java.util.stream.Stream;

public class BasicKeywordCompletion {

    public static final String KEYWORDS = "class | method | field | label | true | false | value | signature |" +
            " extends | public | protected | private | static | final | synthetic | abstract | implements |" +
            " interface | super | enum | bridge | synchronized | volatile | transient | throws | native |" +
            " varargs | strict | outerclass | annotation | nesthost | nestmember | innerclass";

    public static final String DATA_TYPES = "byte | char | double | long | float | int | short | boolean | handle | type | string";

    public static final String FUNCTIONS = "lookupswitch | isub | if_icmpeq | aaload | laload | iconst_m1 | if_icmpne |" +
            " invokevirtual | freturn | lstore | multianewarray | fsub | monitorenter |" +
            " sastore | astore | bastore | if_icmpge | iload | pop2 | putfield | bootstrap |" +
            " anewarray | ldc_dynamic | dup2 | athrow | caload | fconst_0 | if_icmpgt | fconst_1 |" +
            " fdiv | frem | iaload | jsr | sipush | getfield | aconst_null | aload | invokespecial |" +
            " pop | goto | fadd | iflt | permitted | monitorexit | f2d | fneg | castore |" +
            " areturn | f2i | fconst_2 | baload | f2l | ireturn | lsub | ifle | annotation_param_count |" +
            " lcmp | dsub | ifnonnull | lload | if_acmpeq | arraylength | if_acmpne |" +
            " return | fcmpl | ddiv | dneg | constant | dastore | ishr | ladd | imul | ifge |" +
            " newarray | fcmpg | aastore | invokestatic | checkcast | dadd | lneg | ldiv | i2b |" +
            " drem | i2d | i2c | fload | lrem | i2f | ishl | irem | ret | new | fstore |" +
            " i2l | swap | ifne | iadd | dreturn | i2s | invokedynamic | fmul |" +
            " istore | dup_x1 | dup_x2 | ineg | faload | iastore | ifeq | idiv | lushr | dup2_x1 |" +
            " dup2_x2 | lastore | iconst_5 | getstatic | line | putstatic | fastore | daload | nop |" +
            " dmul | dstore | record_component | if_icmple | bipush | parameter | iconst_0 | d2f |" +
            " ifnull | d2i | iconst_3 | lconst_1 | dconst_0 | iconst_4 | lconst_0 | dconst_1 |" +
            " iconst_1 | iconst_2 | l2d | d2l | try_catch | l2f | l2i | lshr | iinc | dcmpl |" +
            " ldc | lmul | dcmpg | if_icmplt | iushr | instanceof | saload | lreturn | tableswitch |" +
            " dload | lshl | ifgt | invokeinterface | dup | local";

    public static void keywords(DefaultCompletionProvider provider) {
        Stream.of(KEYWORDS.split(" \\| ")).forEach(s -> provider.addCompletion(new BasicCompletion(provider, s)));
        Stream.of(DATA_TYPES.split(" \\| ")).forEach(s -> provider.addCompletion(new BasicCompletion(provider, s)));
        Stream.of(FUNCTIONS.split(" \\| ")).forEach(s -> provider.addCompletion(new BasicCompletion(provider, s)));
    }
}
