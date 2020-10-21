package com.github.nickid2018.dynamicex.hacks;

import org.objectweb.asm.*;
import com.github.nickid2018.dynamicex.*;

public class HackGuiWriter extends AbstractHackWriter {

	public HackGuiWriter(ClassWriter writer) {
		super(writer);
	}

	private boolean notFieldAdded = true;

	public static boolean is16 = false;

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if (notFieldAdded) {
			notFieldAdded = false;
			super.visitField(Opcodes.ACC_PRIVATE, "objectScreen",
					"Lcom/github/nickid2018/dynamicex/gui/ObjectInformationsOverlay;", null, null);
		}
		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.equals("<init>"))
			return new HackInitMethod(super.visitMethod(access, name, desc, signature, exceptions));
		if (name.equals(ClassNameTransformer.getMethodName("net.minecraft.client.gui.Gui", "render(F)V"))
				|| (name.equals(ClassNameTransformer.getMethodName("net.minecraft.client.gui.Gui",
						"render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V")))) {
			is16 |= desc.endsWith(";F)V");
			return desc.endsWith("F)V")
					? new HackRenderMethod(super.visitMethod(access, name, desc, signature, exceptions))
					: super.visitMethod(access, name, desc, signature, exceptions);
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	public class HackInitMethod extends MethodVisitor {

		private MethodVisitor defaultVisitor;

		public HackInitMethod(MethodVisitor defaultVisitor) {
			super(Opcodes.ASM6, defaultVisitor);
			this.defaultVisitor = defaultVisitor;
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
			if (opcode == Opcodes.INVOKESPECIAL
					&& owner.equals(ClassNameTransformer.getResourceName("net/minecraft/client/gui/GuiComponent"))) {
//				aload_0->new->dup-> invokespecial->putfield 
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);
				defaultVisitor.visitTypeInsn(Opcodes.NEW,
						"com/github/nickid2018/dynamicex/gui/ObjectInformationsOverlay");
				defaultVisitor.visitInsn(Opcodes.DUP);
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 1);
				defaultVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
						"com/github/nickid2018/dynamicex/gui/ObjectInformationsOverlay", "<init>",
						"(" + ClassNameTransformer.getQualifiedName("net/minecraft/client/Minecraft") + ")V", false);
				defaultVisitor.visitFieldInsn(Opcodes.PUTFIELD,
						ClassNameTransformer.getResourceName("net/minecraft/client/gui/Gui"), "objectScreen",
						"Lcom/github/nickid2018/dynamicex/gui/ObjectInformationsOverlay;");
			}
		}
	}

	public class HackRenderMethod extends MethodVisitor {

		private MethodVisitor defaultVisitor;

		public HackRenderMethod(MethodVisitor defaultVisitor) {
			super(Opcodes.ASM6, defaultVisitor);
			this.defaultVisitor = defaultVisitor;
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
			// After this:
//			1180: aload_0
//		    1181: getfield      #151                // Field subtitleOverlay:Lnet/minecraft/client/gui/components/SubtitleOverlay;
//		    1184: invokevirtual #540                // Method net/minecraft/client/gui/components/SubtitleOverlay.render:()V
			System.out.println(owner + name + desc);
			if (opcode == Opcodes.INVOKEVIRTUAL
					&& owner.equals(
							ClassNameTransformer.getResourceName("net/minecraft/client/gui/components/SubtitleOverlay"))
					&& ((name
							.equals(ClassNameTransformer
									.getMethodName("net.minecraft.client.gui.components.SubtitleOverlay", "render()V"))
							|| (name.equals(ClassNameTransformer.getMethodName(
									"net.minecraft.client.gui.components.SubtitleOverlay",
									"render(Lcom/mojang/blaze3d/vertex/PoseStack;)V")))))) {
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);
				defaultVisitor.visitFieldInsn(Opcodes.GETFIELD,
						ClassNameTransformer.getResourceName("net/minecraft/client/gui/Gui"), "objectScreen",
						"Lcom/github/nickid2018/dynamicex/gui/ObjectInformationsOverlay;");
				if (is16) {
					// PoseStack Version
					defaultVisitor.visitVarInsn(Opcodes.ALOAD, 1);
				} else {
					// No PoseStack Version
					defaultVisitor.visitInsn(Opcodes.ACONST_NULL);
				}
				defaultVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
						"com/github/nickid2018/dynamicex/gui/ObjectInformationsOverlay", "renderGlobal",
						"(Ljava/lang/Object;)V", false);
			}
		}
	}
}
