package io.github.nickid2018.mcde.injector;

import java.util.HashMap;
import java.util.Map;

public class ClassDataRepository {

    private static final ClassDataRepository INSTANCE = new ClassDataRepository();

    public final Map<String, byte[]> classData = new HashMap<>();

    public final Map<String, byte[]> toTransform = new HashMap<>();

    public static ClassDataRepository getInstance() {
        return INSTANCE;
    }

    private ClassDataRepository() {
    }

    public boolean isLoaded(String name) {
        for (Class<?> data : MCProgramInjector.instrumentation.getAllLoadedClasses())
            if (data.getTypeName().equals(name))
                return true;
        return false;
    }
}
