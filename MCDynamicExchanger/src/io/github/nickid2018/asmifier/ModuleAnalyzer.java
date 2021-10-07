package io.github.nickid2018.asmifier;

import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;

public class ModuleAnalyzer extends ModuleVisitor implements Opcodes {

    private final ASMifier asmifier;

    public ModuleAnalyzer(ASMifier asmifier) {
        super(ASM9);
        this.asmifier = asmifier;
    }

    @Override
    public void visitMainClass(String mainClass) {
        asmifier.line("mdv.visitMainClass(%s);", asmifier.quote(mainClass));
    }

    @Override
    public void visitPackage(String packaze) {
        asmifier.line("mdv.visitPackage(%s);", asmifier.quote(packaze));
    }

    @Override
    public void visitRequire(String module, int access, String version) {
        asmifier.line("mdv.visitRequire(%s, %s, %s);", asmifier.quote(module),
                AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.MODULE_REQUIRES), asmifier.quote(version));
    }

    @Override
    public void visitExport(String packaze, int access, String... modules) {
        String mo = asmifier.uncountableToString(modules);
        if(modules == null)
            asmifier.line("mdv.visitExport(%s, %s);", asmifier.quote(packaze),
                AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.MODULE_EXPORT));
        else
            asmifier.line("mdv.visitExport(%s, %s, %s);", asmifier.quote(packaze),
                    AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.MODULE_EXPORT), mo);
    }

    @Override
    public void visitOpen(String packaze, int access, String... modules) {
        String mo = asmifier.uncountableToString(modules);
        if(modules == null)
            asmifier.line("mdv.visitOpen(%s, %s);", asmifier.quote(packaze),
                    AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.MODULE_EXPORT));
        else
            asmifier.line("mdv.visitOpen(%s, %s, %s);", asmifier.quote(packaze),
                    AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.MODULE_EXPORT), mo);
    }

    @Override
    public void visitUse(String service) {
        asmifier.line("mdv.visitUse(%s);", asmifier.quote(service));
    }

    @Override
    public void visitProvide(String service, String... providers) {
        String po = asmifier.uncountableToString(providers);
        if(po == null)
            asmifier.line("mdv.visitProvide(%s);", asmifier.quote(service));
        else
            asmifier.line("mdv.visitProvide(%s, %s);", asmifier.quote(service), po);
    }

    @Override
    public void visitEnd() {
        asmifier.line("mdv.visitEnd();");
        asmifier.indent--;
        asmifier.line("}");
    }
}
