package io.github.nickid2018.mcde.injector;

import java.util.HashMap;
import java.util.Map;

public class ClassDataRepository {

    private static final ClassDataRepository INSTANCE = new ClassDataRepository();

    public final Map<String, byte[]> classData = new HashMap<>();

    public static ClassDataRepository getInstance() {
        return INSTANCE;
    }

    private ClassDataRepository() {
    }
}
