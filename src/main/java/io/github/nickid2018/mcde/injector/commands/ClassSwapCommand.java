package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.nickid2018.mcde.asmdl.ASMDLParser;
import io.github.nickid2018.mcde.injector.ClassWriterHacked;
import io.github.nickid2018.mcde.asmdl.decompile.ClassDecompileVisitor;
import io.github.nickid2018.mcde.injector.ClassDataRepository;
import io.github.nickid2018.mcde.injector.CodingFrame;
import io.github.nickid2018.mcde.injector.InjectorFrame;
import io.github.nickid2018.mcde.injector.MCProgramInjector;
import io.github.nickid2018.mcde.remapper.ClassRemapperFix;
import io.github.nickid2018.mcde.util.ClassUtils;
import io.github.nickid2018.mcde.util.I18N;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.UnmodifiableClassException;

public class ClassSwapCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("classswap")
                .then(InjectCommander.argument("class", StringArgumentType.word())
                        .executes(ClassSwapCommand::swapEdit)
                        .then(InjectCommander.argument("file", StringArgumentType.greedyString())
                                .executes(ClassSwapCommand::swapFile))));
    }

    public static int swapEdit(CommandContext<InjectorFrame> context) {
        String className = StringArgumentType.getString(context, "class");
        String remappedName = ClassUtils.toBinaryName(MCProgramInjector.format.getToSourceMapper().map(className));
        className = ClassUtils.toBinaryName(MCProgramInjector.format.getToNamedMapper().map(remappedName));
        byte[] bytes = ClassDataRepository.getInstance().classData.get(remappedName);
        if (bytes == null) {
            context.getSource().error(I18N.getTranslation("injector.command.swap.notfound", className, remappedName), null);
            return 0;
        }
        ClassReader remapReader = new ClassReader(bytes);
        ClassWriter remapWriter = new ClassWriter(0);
        ClassRemapperFix remapper = new ClassRemapperFix(remapWriter, MCProgramInjector.format.getToNamedMapper());
        remapReader.accept(remapper, 0);

        ClassReader reader = new ClassReader(remapWriter.toByteArray());
        ClassDecompileVisitor visitor = new ClassDecompileVisitor();
        reader.accept(visitor, 0);
        String finalClassName = className;
        CodingFrame frame = new CodingFrame(className + " - " + remappedName, "text/asmdl", true, f -> {
            ASMDLParser parser = new ASMDLParser(f.getCode(),
                    () -> new ClassWriterHacked(ClassWriter.COMPUTE_FRAMES, MCProgramInjector.format.getToSourceMapper()));
            try {
                byte[] data = parser.toClass();
                ClassReader classReader = new ClassReader(data);
                ClassWriter writer = new ClassWriter(0);
                classReader.accept(new ClassRemapperFix(writer, MCProgramInjector.format.getToSourceMapper()), 0);
                data = writer.toByteArray();
                doSwap(remappedName, finalClassName, data, context.getSource());
            } catch (Exception e) {
                context.getSource().error(null, e);
            }
        });
        frame.setCode(visitor.decompiledString());
        frame.show();

        return 0;
    }

    public static int swapFile(CommandContext<InjectorFrame> context) {
        String className = StringArgumentType.getString(context, "class");
        String remappedName = ClassUtils.toBinaryName(MCProgramInjector.format.getToSourceMapper().map(className));
        className = ClassUtils.toBinaryName(MCProgramInjector.format.getToNamedMapper().map(remappedName));
        String fileName = StringArgumentType.getString(context, "file");

        byte[] classData;
        try (InputStream is = new FileInputStream(fileName)) {
            classData = is.readAllBytes();
        } catch (Exception e) {
            context.getSource().error(I18N.getTranslation("error.io"), null);
            return 0;
        }

        ClassReader reader = new ClassReader(classData);
        ClassWriter writer = new ClassWriter(0);
        reader.accept(new ClassRemapperFix(writer, MCProgramInjector.format.getToSourceMapper()), 0);
        classData = writer.toByteArray();

        doSwap(remappedName, className, classData, context.getSource());
        return 0;
    }

    public static void doSwap(String className, String sourceName, byte[] data, InjectorFrame frame) {
        try {
            if (ClassDataRepository.getInstance().isLoaded(className)) {
                ClassDefinition definition = new ClassDefinition(Class.forName(className), data);
                MCProgramInjector.instrumentation.redefineClasses(definition);
                ClassDataRepository.getInstance().classData.put(className, data);
            } else
                ClassDataRepository.getInstance().toTransform.put(className, data);
            frame.info(I18N.getTranslation("injector.command.swap.success", sourceName, className));
        } catch (ClassNotFoundException e) {
            frame.error(I18N.getTranslation("injector.command.swap.notfound", sourceName, className), null);
        } catch (UnmodifiableClassException e) {
            frame.error(I18N.getTranslation("injector.command.swap.unmodifiable", sourceName, className), null);
        }
    }
}
