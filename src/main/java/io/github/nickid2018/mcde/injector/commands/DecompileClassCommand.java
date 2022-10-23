package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.nickid2018.mcde.injector.ClassDataRepository;
import io.github.nickid2018.mcde.injector.ui.CodingFrame;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;
import io.github.nickid2018.mcde.injector.MCProgramInjector;
import io.github.nickid2018.mcde.remapper.ClassRemapperFix;
import io.github.nickid2018.mcde.util.AsyncUtil;
import io.github.nickid2018.mcde.util.ClassUtils;
import io.github.nickid2018.mcde.util.I18N;
import org.apache.commons.io.IOUtils;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class DecompileClassCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("class")
                .then(InjectCommander.argument("class", StringArgumentType.greedyString())
                        .executes(InjectCommander.return0(context -> {
                            String className = StringArgumentType.getString(context, "class");
                            String remappedName = ClassUtils.toBinaryName(MCProgramInjector.format.getToSourceMapper().map(className));
                            className = ClassUtils.toBinaryName(MCProgramInjector.format.getToNamedMapper().map(remappedName));
                            if (ClassDataRepository.getInstance().classData.containsKey(remappedName)) {
                                String finalClassName = className;
                                AsyncUtil.execute(() -> doDecompileAndShow(finalClassName, remappedName, context.getSource()));
                                context.getSource().info(I18N.getTranslation(
                                        "injector.command.class.found", className, remappedName));
                            } else
                                context.getSource().error(I18N.getTranslation(
                                        "injector.command.class.notfound", className, remappedName), null);
                        }))));
    }

    private static void doDecompileAndShow(String className, String remappedName, InjectorFrame frame) {
        try {
            byte[] data = ClassDataRepository.getInstance().classData.get(remappedName);
            CodingFrame codingFrame = new CodingFrame("%s (%s)".formatted(className, remappedName),
                    SyntaxConstants.SYNTAX_STYLE_JAVA, false);

            ClassReader reader = new ClassReader(data);
            ClassWriter writer = new ClassWriter(0);
            reader.accept(new ClassRemapperFix(writer, MCProgramInjector.format.getToNamedMapper()), 0);
            data = writer.toByteArray();

            File input = new File(MCProgramInjector.TEMP_DIR, remappedName + ".class");
            try (FileOutputStream os = new FileOutputStream(input)) {
                os.write(data);
            }

            org.benf.cfr.reader.Main.main(new String[]{
                    input.getAbsolutePath(),
                    "--outputpath", MCProgramInjector.TEMP_DIR.getAbsolutePath(),
                    "--comments", "false",
                    "--silent", "true",
                    "--clobber", "true"
            });
            input.delete();

            File output = new File(MCProgramInjector.TEMP_DIR, ClassUtils.toInternalName(className) + ".java");
            codingFrame.setCode(IOUtils.toString(output.toURI(), StandardCharsets.UTF_8));

            codingFrame.show();
            frame.info(I18N.getTranslation("injector.command.class.success", className, remappedName));
        } catch (Exception e) {
            frame.error(null, e);
        }
    }
}
