package io.github.nickid2018.mcde.injector;

import io.github.nickid2018.mcde.format.MappingFormat;
import io.github.nickid2018.mcde.format.MojangMappingFormat;
import io.github.nickid2018.mcde.format.YarnMappingFormat;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;
import io.github.nickid2018.mcde.remapper.FileProcessor;
import io.github.nickid2018.mcde.util.AsyncUtil;
import io.github.nickid2018.mcde.util.ClassUtils;
import io.github.nickid2018.mcde.util.I18N;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

// Argument Format: <mapping type>;<mapping>;<mc jar file>
public class MCProgramInjector {

    public static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"), "MCDE");
    public static Instrumentation instrumentation;
    public static MappingFormat format;

    public static void premain(String args, Instrumentation instrumentation) throws IOException {
        main(args, instrumentation);
    }

    public static void agentmain(String args, Instrumentation instrumentation) throws IOException {
        main(args, instrumentation);
    }

    private static void main(String args, Instrumentation instrumentation) throws IOException {
        MCProgramInjector.instrumentation = instrumentation;
        createMapper(args);
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader,
                                    String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                ClassDataRepository.getInstance().classData.put(ClassUtils.toBinaryName(className), classfileBuffer);
                return null;
            }
        });
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader,
                                    String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (ClassDataRepository.getInstance().toTransform.containsKey(className))
                    return ClassDataRepository.getInstance().toTransform.remove(className);
                return null;
            }
        });

        if (!TEMP_DIR.isDirectory())
            TEMP_DIR.mkdirs();

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/asmdl", "io.github.nickid2018.mcde.asmdl.highlight.ASMDLTokenMaker");
        FoldParserManager.get().addFoldParserMapping("text/asmdl", new CurlyFoldParser(false, false));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (TEMP_DIR.list() != null)
                Stream.of(Objects.requireNonNull(TEMP_DIR.list())).forEach(s -> new File(TEMP_DIR, s).delete());
        }));

        InjectorFrame.INSTANCE.show();
        AsyncUtil.start();
    }

    private static void createMapper(String data) throws IOException {
        String[] typeAndPath = data.split(";", 3);
        if (typeAndPath.length != 3)
            throw new IllegalArgumentException(I18N.getTranslation("error.argument"));
        format = switch (typeAndPath[0].toLowerCase(Locale.ROOT)) {
            case "mojang" -> new MojangMappingFormat(Files.newInputStream(Path.of(typeAndPath[1])));
            case "yarn" -> new YarnMappingFormat(Files.newInputStream(Path.of(typeAndPath[1])));
            default -> throw new IllegalArgumentException(I18N.getTranslation("error.mapping.unsupported"));
        };
        try (ZipFile file = new ZipFile(typeAndPath[2])) {
            FileProcessor.addPlainClasses(file, format);
            FileProcessor.generateInheritTree(file, format);
        }
        format.createToSourceMapper();
    }

    public static void swapClass(ClassDefinition definition) throws UnmodifiableClassException, ClassNotFoundException {
        instrumentation.redefineClasses(definition);
    }
}
