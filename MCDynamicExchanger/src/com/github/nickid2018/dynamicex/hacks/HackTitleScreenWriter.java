package com.github.nickid2018.dynamicex.hacks;

import org.objectweb.asm.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.dynamicex.gui.*;

public class HackTitleScreenWriter extends AbstractHackWriter {

	public HackTitleScreenWriter(ClassWriter writer) {
		super(writer);
	}

	private static int x = Integer.MIN_VALUE;

	public static void render(Object mayPoseStack, Font font) {
		if (x == Integer.MIN_VALUE) {
			x = (Minecraft.getInstance().screen.width
					- font.width("Warning: This JAR has been modified by MCDynamicExchanger")) / 2;
		}
		RenderInterface.renderString(mayPoseStack, font, "Warning: This JAR has been modified by MCDynamicExchanger", x,
				5, 0xFF0000);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.equals(
				ClassNameTransformer.getMethodName("net.minecraft.client.gui.screens.TitleScreen", "render(IIF)V"))
				|| name.equals(ClassNameTransformer.getMethodName("net.minecraft.client.gui.screens.TitleScreen",
						"render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V"))) {
			return new HackRenderMethod(writer.visitMethod(access, name, desc, signature, exceptions),
					desc.endsWith(";IIF)V"));
		}
		return writer.visitMethod(access, name, desc, signature, exceptions);
	}

	public class HackRenderMethod extends MethodVisitor {

		private MethodVisitor defaultVisitor;
		private boolean is16;
		private boolean write = true;

		public HackRenderMethod(MethodVisitor defaultVisitor, boolean is16) {
			super(Opcodes.ASM6, defaultVisitor);
			this.is16 = is16;
			this.defaultVisitor = defaultVisitor;
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
			if (write && owner
					.equals(ClassNameTransformer.getResourceName("net/minecraft/client/gui/screens/TitleScreen"))) {
				if (is16) {
					if (name.equals(ClassNameTransformer.getMethodName("net.minecraft.client.gui.screens.TitleScreen",
							"drawString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))) {
						write = false;
						defaultVisitor.visitVarInsn(Opcodes.ALOAD, 1);
						defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);
						defaultVisitor.visitFieldInsn(Opcodes.GETFIELD,
								ClassNameTransformer.getResourceName("net/minecraft/client/gui/screens/TitleScreen"),
								ClassNameTransformer.getFieldName("net.minecraft.client.gui.screens.TitleScreen",
										"font"),
								ClassNameTransformer.getQualifiedName("net/minecraft/client/gui/Font"));
						defaultVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
								"com/github/nickid2018/dynamicex/hacks/HackTitleScreenWriter", "render",
								"(Ljava/lang/Object;"
										+ ClassNameTransformer.getQualifiedName("net/minecraft/client/gui/Font") + ")V",
								false);
					}
				} else {
					if (name.equals(ClassNameTransformer.getMethodName("net.minecraft.client.gui.screens.TitleScreen",
							"drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))) {
						write = false;
						defaultVisitor.visitInsn(Opcodes.ACONST_NULL);
						defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);
						defaultVisitor.visitFieldInsn(Opcodes.GETFIELD,
								ClassNameTransformer.getResourceName("net/minecraft/client/gui/screens/TitleScreen"),
								ClassNameTransformer.getFieldName("net.minecraft.client.gui.screens.TitleScreen",
										"font"),
								ClassNameTransformer.getQualifiedName("net/minecraft/client/gui/Font"));
						defaultVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
								"com/github/nickid2018/dynamicex/hacks/HackTitleScreenWriter", "render",
								"(Ljava/lang/Object;"
										+ ClassNameTransformer.getQualifiedName("net/minecraft/client/gui/Font") + ")V",
								false);
					}
				}
			}
		}
	}
}
