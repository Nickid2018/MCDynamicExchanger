package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.nickid2018.mcde.injector.ClassDataRepository;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;
import io.github.nickid2018.mcde.injector.MCProgramInjector;
import io.github.nickid2018.mcde.injector.ui.WorkbenchFrame;
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
        } catch (Throwable e) {
            InjectorFrame.INSTANCE.error(I18N.getTranslation("injector.workbench.swap.error"), e);
        }
    });

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("swap")
                .executes(InjectCommander.return0(injector -> frame.show())));
    }
}
