package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.nickid2018.mcde.injector.ClassDataRepository;
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
