package io.github.nickid2018.mcremap;

import io.github.nickid2018.util.ClassUtils;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Remapper;

public class ASMRemapper extends Remapper {

    private final RemapperFormat format;

    public ASMRemapper(RemapperFormat format) {
        this.format = format;
    }

    // --- Fix a bug in ASM #317954

    private Type mapType(final Type type) {
        switch (type.getSort()) {
            case Type.ARRAY:
                StringBuilder remappedDescriptor = new StringBuilder();
                for (int i = 0; i < type.getDimensions(); ++i) {
                    remappedDescriptor.append('[');
                }
                remappedDescriptor.append(mapType(type.getElementType()).getDescriptor());
                return Type.getType(remappedDescriptor.toString());
            case Type.OBJECT:
                String remappedInternalName = map(type.getInternalName());
                return remappedInternalName != null ? Type.getObjectType(remappedInternalName) : type;
            case Type.METHOD:
                return Type.getMethodType(mapMethodDesc(type.getDescriptor()));
            default:
                return type;
        }
    }

    @Override
    public Object mapValue(Object value) {
        if (value instanceof Type) {
            return mapType((Type) value);
        }
        if (value instanceof Handle) {
            Handle handle = (Handle) value;
            boolean isField = handle.getTag() <= Opcodes.H_PUTSTATIC;
            return new Handle(
                    handle.getTag(),
                    mapType(handle.getOwner()),
                    // ! Fix handle field
                    isField ? mapFieldName(handle.getOwner(), handle.getName(), handle.getDesc())
                            : mapMethodName(handle.getOwner(), handle.getName(), handle.getDesc()),
                    isField ? mapDesc(handle.getDesc())
                            : mapMethodDesc(handle.getDesc()),
                    handle.isInterface());
        }
        if (value instanceof ConstantDynamic) {
            ConstantDynamic constantDynamic = (ConstantDynamic) value;
            int bootstrapMethodArgumentCount = constantDynamic.getBootstrapMethodArgumentCount();
            Object[] remappedBootstrapMethodArguments = new Object[bootstrapMethodArgumentCount];
            for (int i = 0; i < bootstrapMethodArgumentCount; ++i) {
                remappedBootstrapMethodArguments[i] =
                        mapValue(constantDynamic.getBootstrapMethodArgument(i));
            }
            String descriptor = constantDynamic.getDescriptor();
            return new ConstantDynamic(
                    mapInvokeDynamicMethodName(constantDynamic.getName(), descriptor),
                    mapDesc(descriptor),
                    (Handle) mapValue(constantDynamic.getBootstrapMethod()),
                    remappedBootstrapMethodArguments);
        }
        return value;
    }

    // --- Fix End

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        RemapClass clazz = format.remaps.get(ClassUtils.toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findMethod(name + desc);
        return get == null ? name : get;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        RemapClass clazz = format.remaps.get(ClassUtils.toBinaryName(owner));
        if (clazz == null)
            return name;
        String get = clazz.findField(name + desc);
        return get == null ? name : get;
    }

    @Override
    public String map(String typeName) {
        RemapClass clazz = format.remaps.get(ClassUtils.toBinaryName(typeName));
        return clazz == null ? typeName : ClassUtils.toInternalName(clazz.mapName());
    }
}
