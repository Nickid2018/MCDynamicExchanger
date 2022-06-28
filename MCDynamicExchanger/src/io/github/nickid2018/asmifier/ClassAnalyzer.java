package io.github.nickid2018.asmifier;

import org.objectweb.asm.*;

public class ClassAnalyzer extends ClassVisitor implements Opcodes {

    private final ASMifier asmifier;

    public ClassAnalyzer(ASMifier asmifier) {
        super(ASM9);
        this.asmifier = asmifier;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String versionString = version + "";
        if (!asmifier.noConvertConstants)
            if (version < 53)
                versionString = "V1_" + (version - 44);
            else
                versionString = "V" + (version - 44);
        asmifier.line("cw.visit(%s, %s, %s, %s, %s, %s);",
                versionString, AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.CLASS, asmifier),
                asmifier.quote(name), asmifier.quote(signature), asmifier.quote(superName), asmifier.arrayToString(interfaces));
    }

    @Override
    public void visitSource(String source, String debug) {
        if (!asmifier.noExtraCodes)
            asmifier.line("cw.visitSource(%s, %s);", asmifier.quote(source), asmifier.quote(debug));
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("ModuleVisitor mdv = cw.visitModule(%s, %s, %s);",
                asmifier.quote(name), AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.MODULE, asmifier),
                asmifier.quote(version));
        return new ModuleAnalyzer(asmifier);
    }

    @Override
    public void visitNestHost(String nestHost) {
        asmifier.line("cw.visitNestHost(%s);", asmifier.quote(nestHost));
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        asmifier.line("cw.visitOuterClass(%s, %s, %s);", asmifier.quote(owner), asmifier.quote(name), asmifier.quote(descriptor));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = cw.visitAnnotation(%s, %s);", asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        String typeRefStr = typeRef + "";
        if (!asmifier.noConvertConstants)
            switch (typeRef) {
                case TypeReference.CLASS_TYPE_PARAMETER:
                    typeRefStr = "TypeReference.CLASS_TYPE_PARAMETER";
                    break;
                case TypeReference.CLASS_TYPE_PARAMETER_BOUND:
                    typeRefStr = "TypeReference.CLASS_TYPE_PARAMETER_BOUND";
                    break;
                case TypeReference.CLASS_EXTENDS:
                    typeRefStr = "TypeReference.CLASS_EXTENDS";
                    break;
            }
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = cw.visitTypeAnnotation(%s, TypePath.fromString(%s), %s, %s);",
                typeRefStr, asmifier.quote(typePath.toString()), asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        // Unsupported
    }

    @Override
    public void visitNestMember(String nestMember) {
        asmifier.line("cw.visitNestMember(%s);", asmifier.quote(nestMember));
    }

    @Override
    public void visitPermittedSubclass(String permittedSubclass) {
        asmifier.line("cw.visitPermittedSubclass(%s);", asmifier.quote(permittedSubclass));
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        asmifier.line("cw.visitInnerClass(%s, %s, %s, %s);",
                asmifier.quote(name), asmifier.quote(outerName), asmifier.quote(innerName),
                AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.CLASS, asmifier));
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("RecordComponentVisitor rcv = cw.visitRecordComponent(%s, %s, %s);",
                asmifier.quote(name), asmifier.quote(descriptor), asmifier.quote(signature));
        return new RecordComponentAnalyzer(asmifier);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        asmifier.line("{");
        asmifier.indent++;
        String valueStr;
        if (value == null)
            valueStr = "null";
        else if (value instanceof String)
            valueStr = asmifier.quote(value.toString());
        else if (value instanceof Double)
            valueStr = value + "D";
        else if (value instanceof Long)
            valueStr = value + "L";
        else
            valueStr = value.toString();
        asmifier.line("FieldVisitor fv = cw.visitField(%s, %s, %s, %s, %s);",
                AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.FIELD, asmifier),
                asmifier.quote(name), asmifier.quote(descriptor), asmifier.quote(signature), valueStr);
        return new FieldAnalyzer(asmifier);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("MethodVisitor mv = cw.visitMethod(%s, %s, %s, %s, %s);",
                AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.METHOD, asmifier),
                asmifier.quote(name), asmifier.quote(descriptor), asmifier.quote(signature), asmifier.arrayToString(exceptions));
        return new MethodAnalyzer(asmifier);
    }

    @Override
    public void visitEnd() {
        asmifier.line("cw.visitEnd();");
    }
}
