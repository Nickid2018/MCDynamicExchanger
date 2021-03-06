package com.github.nickid2018.dynamicex.hacks;

import org.objectweb.asm.*;
import com.github.nickid2018.dynamicex.*;

public class HackRenderInterfaceWriter extends AbstractHackWriter {

	public HackRenderInterfaceWriter(ClassWriter writer) {
		super(writer);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new HackMethod(super.visitMethod(access, name, desc, signature, exceptions), name);
	}

	public class HackMethod extends MethodVisitor {

		public HackMethod(MethodVisitor defaultVisitor, String name) {
			super(Opcodes.ASM6, name.equals("<init>") ? defaultVisitor : null);
			if (name.equals("renderString")) {
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 1);
				if (HackGuiWriter.is16) {
					defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);
					defaultVisitor.visitTypeInsn(Opcodes.CHECKCAST,
							ClassNameTransformer.getQualifiedName("com/mojang/blaze3d/vertex/PoseStack"));
				}
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 2);
				defaultVisitor.visitVarInsn(Opcodes.FLOAD, 3);
				defaultVisitor.visitVarInsn(Opcodes.FLOAD, 4);
				defaultVisitor.visitVarInsn(Opcodes.ILOAD, 5);
				defaultVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
						ClassNameTransformer.getResourceName("net/minecraft/client/gui/Font"),
						ClassNameTransformer.getMethodName("net.minecraft.client.gui.Font",
								"draw" + (HackGuiWriter.is16
										? "(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/lang/String;FFI)I"
										: "(Ljava/lang/String;FFI)I")),
						HackGuiWriter.is16
								? "(" + ClassNameTransformer.getQualifiedName("com/mojang/blaze3d/vertex/PoseStack")
										+ "Ljava/lang/String;FFI)I"
								: "(Ljava/lang/String;FFI)I",
						false);
				defaultVisitor.visitInsn(Opcodes.POP);
				defaultVisitor.visitInsn(Opcodes.RETURN);
				defaultVisitor.visitMaxs(6, 6);
				defaultVisitor.visitEnd();
			}
			if (name.equals("renderFill")) {
				if (HackGuiWriter.is16) {
					defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);
					defaultVisitor.visitTypeInsn(Opcodes.CHECKCAST,
							ClassNameTransformer.getQualifiedName("com/mojang/blaze3d/vertex/PoseStack"));
				}
				defaultVisitor.visitVarInsn(Opcodes.ILOAD, 1);
				defaultVisitor.visitVarInsn(Opcodes.ILOAD, 2);
				defaultVisitor.visitVarInsn(Opcodes.ILOAD, 3);
				defaultVisitor.visitVarInsn(Opcodes.ILOAD, 4);
				defaultVisitor.visitVarInsn(Opcodes.ILOAD, 5);
				defaultVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
						ClassNameTransformer.getResourceName("net/minecraft/client/gui/GuiComponent"),
						ClassNameTransformer.getMethodName("net.minecraft.client.gui.GuiComponent", "fill"
								+ (HackGuiWriter.is16 ? "(Lcom/mojang/blaze3d/vertex/PoseStack;IIIII)V" : "(IIIII)V")),
						HackGuiWriter.is16
								? "(" + ClassNameTransformer.getQualifiedName("com/mojang/blaze3d/vertex/PoseStack")
										+ "IIIII)V"
								: "(IIIII)V",
						false);
				defaultVisitor.visitInsn(Opcodes.RETURN);
				defaultVisitor.visitMaxs(6, 6);
				defaultVisitor.visitEnd();
			}
		}
	}
}
