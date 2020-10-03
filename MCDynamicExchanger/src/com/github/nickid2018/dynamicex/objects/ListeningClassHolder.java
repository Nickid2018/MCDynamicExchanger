package com.github.nickid2018.dynamicex.objects;

import org.objectweb.asm.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.dynamicex.debug.*;

public class ListeningClassHolder {

	public static void addGlobalTickListen(String name, int x, int y) throws Exception {
		Object obj = ObjectRunningStack.INSTANCE.popObject();
		ObjectOnlyElement element;
		Class<?> now;
		if (obj instanceof ObjectProvider) {
			ObjectProvider<?> provider = (ObjectProvider<?>) obj;
			element = new ObjectOnlyElement(now = provider.getObject().getClass(), provider);
		} else {
			element = new ObjectOnlyElement(now = obj.getClass(), new StaticObjectProvider<>(obj));
		}
		AddAfterTickingWriter writer = new AddAfterTickingWriter(ClassUtils.getClassBytes(now.getTypeName(), true),
				mv -> {
					mv.visitLdcInsn(name);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitLdcInsn(name);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC,
							"com/github/nickid2018/dynamicex/objects/ObjectInfosHolder", "putInfo",
							"(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z", false);
					mv.visitInsn(Opcodes.POP);
				}, now.getTypeName());
		DynamicClassesHolder.exchangeClassData(now.getTypeName(), writer.toByteArray());
		ObjectInfosHolder.elements.put(name, element);
		element.x0 = x;
		element.y0 = y;
	}
}
