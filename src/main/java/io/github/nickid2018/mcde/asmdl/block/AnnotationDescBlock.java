package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.*;

import java.util.Map;
import java.util.stream.Stream;

public class AnnotationDescBlock extends DescBlock {

    public AnnotationDescBlock() {
        super("annotation");
    }

    public static final Map<String, Integer> METHOD_TYPE_REFERENCE_KINDS = Map.of(
            "return", TypeReference.METHOD_RETURN,
            "parameter", TypeReference.METHOD_FORMAL_PARAMETER,
            "throws", TypeReference.THROWS,
            "type_parameter", TypeReference.METHOD_TYPE_PARAMETER,
            "type_parameter_bound", TypeReference.METHOD_TYPE_PARAMETER_BOUND,
            "receiver", TypeReference.METHOD_RECEIVER
    );

    public static final Map<String, Integer> LOCAL_TYPE_REFERENCE_KINDS = Map.of(
            "local_variable", TypeReference.LOCAL_VARIABLE,
            "resource_variable", TypeReference.RESOURCE_VARIABLE
    );

    public static final Map<String, Integer> INSN_TYPE_REFERENCE_KINDS = Map.of(
            "new", TypeReference.NEW,
            "cast", TypeReference.CAST,
            "constructor_reference", TypeReference.CONSTRUCTOR_REFERENCE,
            "method_reference", TypeReference.METHOD_REFERENCE,
            "instanceof", TypeReference.INSTANCEOF,
            "constructor_invocation_type_argument", TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT,
            "method_invocation_type_argument", TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT,
            "constructor_reference_type_argument", TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT,
            "method_reference_type_argument", TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT
    );

    public static final Map<String, Integer> CLASS_TYPE_REFERENCE_KINDS = Map.of(
            "class_parameter", TypeReference.CLASS_TYPE_PARAMETER,
            "class_parameter_bound", TypeReference.CLASS_TYPE_PARAMETER_BOUND,
            "class_extends", TypeReference.CLASS_EXTENDS
    );



    @Override
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
                    yield visitor.visitTypeAnnotation(TypeReference.newTypeReference(TypeReference.FIELD).getValue(), path, desc, visible);
                }
                default -> throw new ASMDLSyntaxException("unknown annotation type: " + args[0]);
            };
        if (context.environment() == DescFunctions.METHOD || context.environment() == DescFunctions.LABEL) {
            return switch (args[0]) {
                case "method" -> {
                    if (args.length != 3)
                        throw new ASMDLSyntaxException("annotation method needs 2 arguments");
                    MethodVisitor visitor = (MethodVisitor) context.visitor();
                    yield visitor.visitAnnotation(args[1], Boolean.parseBoolean(args[2]));
                }
                case "method_type" -> {
                    if (args.length != 5)
                        throw new ASMDLSyntaxException("annotation method_type needs 4 argument");
                    MethodVisitor visitor = (MethodVisitor) context.visitor();
                    String[] typeRef = args[1].split(",");

                    if (!METHOD_TYPE_REFERENCE_KINDS.containsKey(typeRef[0]))
                        throw new ASMDLSyntaxException("unknown method type reference kind: " + args[1]);
                    int kind = METHOD_TYPE_REFERENCE_KINDS.get(typeRef[0]);

                    int refInt = (switch (kind) {
                        case TypeReference.METHOD_RETURN, TypeReference.THROWS, TypeReference.METHOD_RECEIVER -> TypeReference.newTypeReference(kind);
                        case TypeReference.METHOD_FORMAL_PARAMETER -> TypeReference.newFormalParameterReference(Integer.parseInt(typeRef[1]));
                        case TypeReference.METHOD_TYPE_PARAMETER -> TypeReference.newTypeParameterReference(kind, Integer.parseInt(typeRef[1]));
                        case TypeReference.METHOD_TYPE_PARAMETER_BOUND -> TypeReference.newTypeParameterBoundReference(kind, Integer.parseInt(typeRef[1]), Integer.parseInt(typeRef[2]));
                        default -> throw new ASMDLSyntaxException("unknown method type reference: " + args[1]);
                    }).getValue();

                    TypePath path = TypePath.fromString(args[2]);
                    String desc = args[3];
                    boolean visible = Boolean.parseBoolean(args[4]);
                    yield visitor.visitTypeAnnotation(refInt, path, desc, visible);
                }
                case "default" -> {
                    if (args.length != 1)
                        throw new ASMDLSyntaxException("annotation default has no arguments");
                    MethodVisitor visitor = (MethodVisitor) context.visitor();
                    yield visitor.visitAnnotationDefault();
                }
                case "parameter" -> {
                    if (args.length != 4)
                        throw new ASMDLSyntaxException("annotation parameter needs 3 arguments");
                    MethodVisitor visitor = (MethodVisitor) context.visitor();
                    yield visitor.visitParameterAnnotation(Integer.parseInt(args[1]), args[2], Boolean.parseBoolean(args[3]));
                }
                case "try_catch" -> {
                    if (args.length != 5)
                        throw new ASMDLSyntaxException("annotation try_catch needs 5 arguments");
                    MethodVisitor visitor = (MethodVisitor) context.visitor();
                    yield visitor.visitTryCatchAnnotation(TypeReference.newExceptionReference(Integer.parseInt(args[1])).getValue(),
                            TypePath.fromString(args[2]), args[3], Boolean.parseBoolean(args[4]));
                }
                case "local" -> {
                    if (args.length != 8)
                        throw new ASMDLSyntaxException("annotation local needs 7 arguments");
                    MethodVisitor visitor = (MethodVisitor) context.visitor();
                    if (!LOCAL_TYPE_REFERENCE_KINDS.containsKey(args[1]))
                        throw new ASMDLSyntaxException("unknown local type reference kind: " + args[1]);
                    int kind = LOCAL_TYPE_REFERENCE_KINDS.get(args[1]);
                    Label[] starts = Stream.of(args[3].split(","))
                            .map(s -> context.labelMap().computeIfAbsent(s, k -> new Label()))
                            .toArray(Label[]::new);
                    Label[] ends = Stream.of(args[4].split(","))
                            .map(s -> context.labelMap().computeIfAbsent(s, k -> new Label()))
                            .toArray(Label[]::new);
                    int[] labelsInts;
                    try {
                        labelsInts = Stream.of(args[5].split(","))
                                .mapToInt(Integer::parseInt)
                                .toArray();
                    } catch (Exception e) {
                        throw new ASMDLSyntaxException("local requires an integer sequence");
                    }
                    yield visitor.visitLocalVariableAnnotation(TypeReference.newTypeReference(kind).getValue(),
                            TypePath.fromString(args[2]), starts, ends, labelsInts, args[6], Boolean.parseBoolean(args[7]));
                }
                case "insn" -> {
                    if (args.length != 5)
                        throw new ASMDLSyntaxException("annotation insn needs 4 arguments");
                    MethodVisitor visitor = (MethodVisitor) context.visitor();

                    String[] typeRef = args[1].split(",");

                    if (!INSN_TYPE_REFERENCE_KINDS.containsKey(typeRef[0]))
                        throw new ASMDLSyntaxException("unknown method type reference kind: " + args[1]);
                    int kind = INSN_TYPE_REFERENCE_KINDS.get(typeRef[0]);

                    int refInt = (switch (kind) {
                        case TypeReference.NEW, TypeReference.INSTANCEOF,
                                TypeReference.CONSTRUCTOR_REFERENCE, TypeReference.METHOD_REFERENCE ->
                                TypeReference.newTypeReference(kind);
                        case TypeReference.CAST, TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT,
                                TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT, TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT,
                                TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT -> TypeReference.newTypeArgumentReference(kind, Integer.parseInt(typeRef[1]));
                        default -> throw new ASMDLSyntaxException("unknown method type reference: " + args[1]);
                    }).getValue();

                    yield visitor.visitInsnAnnotation(refInt, TypePath.fromString(args[2]), args[3], Boolean.parseBoolean(args[4]));
                }
                default -> throw new ASMDLSyntaxException("unknown annotation type: " + args[0]);
            };
        }
        if (context.environment() == DescFunctions.CLASS) {
            return switch (args[0]) {
                case "class" -> {
                    if (args.length != 3)
                        throw new ASMDLSyntaxException("annotation class needs 2 arguments");
                    ClassVisitor visitor = (ClassVisitor) context.visitor();
                    yield visitor.visitAnnotation(args[1], Boolean.parseBoolean(args[2]));
                }
                case "class_type" -> {
                    if (args.length != 5)
                        throw new ASMDLSyntaxException("annotation class_type needs 4 argument");
                    ClassVisitor visitor = (ClassVisitor) context.visitor();

                    String[] typeRef = args[1].split(",");

                    if (!CLASS_TYPE_REFERENCE_KINDS.containsKey(typeRef[0]))
                        throw new ASMDLSyntaxException("unknown method type reference kind: " + args[1]);
                    int kind = CLASS_TYPE_REFERENCE_KINDS.get(typeRef[0]);

                    int refInt = (switch (kind) {
                        case TypeReference.CLASS_TYPE_PARAMETER -> TypeReference.newTypeParameterReference(kind, Integer.parseInt(typeRef[1]));
                        case TypeReference.CLASS_TYPE_PARAMETER_BOUND -> TypeReference.newTypeParameterBoundReference(kind, Integer.parseInt(typeRef[1]), Integer.parseInt(typeRef[2]));
                        case TypeReference.CLASS_EXTENDS -> TypeReference.newSuperTypeReference(kind);
                        default -> throw new ASMDLSyntaxException("unknown method type reference: " + args[1]);
                    }).getValue();

                    TypePath path = TypePath.fromString(args[2]);
                    String desc = args[3];
                    boolean visible = Boolean.parseBoolean(args[4]);
                    yield visitor.visitTypeAnnotation(refInt, path, desc, visible);
                }
                default -> throw new ASMDLSyntaxException("unknown annotation type: " + args[0]);
            };
        }
        if (context.environment() == DescFunctions.RECORD_COMPONENT) {
            return switch (args[0]) {
                case "record_component" -> {
                    if (args.length != 3)
                        throw new ASMDLSyntaxException("annotation record_component needs 2 arguments");
                    RecordComponentVisitor visitor = (RecordComponentVisitor) context.visitor();
                    yield visitor.visitAnnotation(args[1], Boolean.parseBoolean(args[2]));
                }
                case "record_component_type" -> {
                    if (args.length != 5)
                        throw new ASMDLSyntaxException("annotation record_component_type needs 4 argument");
                    RecordComponentVisitor visitor = (RecordComponentVisitor) context.visitor();

                    String[] typeRef = args[1].split(",");

                    if (!CLASS_TYPE_REFERENCE_KINDS.containsKey(typeRef[0]))
                        throw new ASMDLSyntaxException("unknown method type reference kind: " + args[1]);
                    int kind = CLASS_TYPE_REFERENCE_KINDS.get(typeRef[0]);

                    int refInt = (switch (kind) {
                        case TypeReference.CLASS_TYPE_PARAMETER -> TypeReference.newTypeParameterReference(kind, Integer.parseInt(typeRef[1]));
                        case TypeReference.CLASS_TYPE_PARAMETER_BOUND -> TypeReference.newTypeParameterBoundReference(kind, Integer.parseInt(typeRef[1]), Integer.parseInt(typeRef[2]));
                        case TypeReference.CLASS_EXTENDS -> TypeReference.newSuperTypeReference(kind);
                        default -> throw new ASMDLSyntaxException("unknown method type reference: " + args[1]);
                    }).getValue();

                    TypePath path = TypePath.fromString(args[2]);
                    String desc = args[3];
                    boolean visible = Boolean.parseBoolean(args[4]);
                    yield visitor.visitTypeAnnotation(refInt, path, desc, visible);
                }
                default -> throw new ASMDLSyntaxException("unknown annotation type: " + args[0]);
            };
        }
        throw new ASMDLSyntaxException("annotation block can only be used in annotation, class," +
                " method(label) or field(record component) environment");
    }

    @Override
    public Object processEnd(DescFunctionContext context) {
        ((AnnotationVisitor) context.visitor()).visitEnd();
        return null;
    }
}
