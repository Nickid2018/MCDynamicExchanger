package io.github.nickid2018.mcde.format;

import io.github.nickid2018.mcde.remapper.ASMRemapper;
import io.github.nickid2018.mcde.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class MappingFormat {

    protected final Map<String, MappingClassData> remaps = new HashMap<>();
    protected final Map<String, MappingClassData> revMap = new HashMap<>();

    protected ASMRemapper toSourceMapper;
    protected ASMRemapper toNamedMapper;

    public ASMRemapper getToNamedMapper() {
        return toNamedMapper;
    }

    public ASMRemapper getToSourceMapper() {
        return toSourceMapper;
    }

    public void addRemapClass(String name, MappingClassData clazz) {
        remaps.put(name, clazz);
        revMap.put(toNamedMapper.map(name), createRevClass(clazz, revMap));
    }

    public MappingClassData getToNamedClass(String source) {
        return remaps.getOrDefault(source, new MappingClassData(source, source));
    }

    public void createToSourceMapper() {
        for (String clazz : remaps.keySet()) {
            String className = ClassUtils.toBinaryName(toNamedMapper.map(clazz));
            if (!revMap.containsKey(className))
                revMap.put(className, createRevClass(remaps.get(clazz), revMap));
        }
        toSourceMapper = new ASMRemapper(revMap);
    }

    private MappingClassData createRevClass(MappingClassData source, Map<String, MappingClassData> revMap) {
        MappingClassData created = new MappingClassData(source.remapName, source.sourceName);
        for (Map.Entry<String, String> en : source.fieldMappings.entrySet()) {
            String[] fieldAndDesc = en.getKey().split("\\+");
            String sourceName = fieldAndDesc[0];
            String remappedDesc = toNamedMapper.mapDesc(fieldAndDesc[1]);
            String remappedName = en.getValue();
            created.fieldMappings.put(remappedName + "+" + remappedDesc, sourceName);
        }
        for (Map.Entry<String, String> en : source.methodMappings.entrySet()) {
            String[] methodAndDesc = en.getKey().split("\\(");
            String sourceName = methodAndDesc[0];
            String remappedDesc = toNamedMapper.mapDesc("(" + methodAndDesc[1]);
            String remappedName = en.getValue();
            created.methodMappings.put(remappedName + remappedDesc, sourceName);
        }
        for (MappingClassData superClass : source.superClasses) {
            String className = superClass.remapName;
            if (!revMap.containsKey(className))
                revMap.put(className, createRevClass(superClass, revMap));
            created.superClasses.add(revMap.get(className));
        }
        return created;
    }
}
