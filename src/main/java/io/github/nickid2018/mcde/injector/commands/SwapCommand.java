package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.nickid2018.mcde.injector.*;
import io.github.nickid2018.mcde.util.I18N;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.UnmodifiableClassException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SwapCommand {

    private static final WorkbenchFrame frame = new WorkbenchFrame(I18N.getTranslation("injector.workbench.swap"), map -> {
        Set<ClassDefinition> definitions = new HashSet<>();
        try {
            for (Map.Entry<String, byte[]> entry : map.entrySet())
                definitions.add(new ClassDefinition(Class.forName(entry.getKey()), entry.getValue()));
            MCProgramInjector.instrumentation.redefineClasses(definitions.toArray(new ClassDefinition[0]));
            definitions.forEach(d -> ClassDataRepository.getInstance().classData.put(
                    d.getDefinitionClass().getName(), d.getDefinitionClassFile()));
            InjectorFrame.INSTANCE.info(I18N.getTranslation("injector.workbench.swap.success"));
        } catch (UnmodifiableClassException e) {
            InjectorFrame.INSTANCE.error(I18N.getTranslation("injector.workbench.swap.unmodifiable"), e);
        } catch (Exception e) {
            InjectorFrame.INSTANCE.error(I18N.getTranslation("injector.workbench.swap.error"), e);
        }
    });

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("swap")
                .executes(context -> {
                    frame.show();
                    return 0;
                }));
    }
}
