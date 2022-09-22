package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

public class AnnotationDescBlock extends DescBlock {

    public AnnotationDescBlock() {
        super("annotation");
    }

    @Override
    // field:
    //  annotation field <name> <desc>
    public AnnotationVisitor processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        String[] args = context.args();
        if (context.environment() == DescFunctions.ANNOTATION) {
            if (context.args().length != 2)
                throw new ASMDLSyntaxException("annotation needs 2 arguments");
            return ((AnnotationVisitor) context.visitor()).visitAnnotation(args[0], args[1]);
        }
        if (context.environment() == DescFunctions.FIELD)
            return switch (args[0]) {
                case "field" -> {
                    if (args.length != 3)
                        throw new ASMDLSyntaxException("annotation field needs 2 arguments");
                    FieldVisitor visitor = (FieldVisitor) context.visitor();
                    yield visitor.visitAnnotation(args[1], Boolean.parseBoolean(args[2]));
                }
                case "field_type" -> {
                    if (args.length != 4)
                        throw new ASMDLSyntaxException("annotation field_type needs 3 argument");
                    FieldVisitor visitor = (FieldVisitor) context.visitor();
                    TypePath path = TypePath.fromString(args[1]);
                    String desc = args[2];
                    boolean visible = Boolean.parseBoolean(args[3]);
                    yield visitor.visitTypeAnnotation(TypeReference.FIELD, path, desc, visible);
                }
                default -> throw new ASMDLSyntaxException("unknown annotation type: " + args[0]);
            };
        throw new ASMDLSyntaxException("annotation block can only be used in annotation, class, method or field");
    }

    @Override
    public Object processEnd(DescFunctionContext context) {
        ((AnnotationVisitor) context.visitor()).visitEnd();
        return null;
    }
}
