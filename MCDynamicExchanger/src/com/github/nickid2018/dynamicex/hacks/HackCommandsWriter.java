package com.github.nickid2018.dynamicex.hacks;

import org.objectweb.asm.*;

public class HackCommandsWriter extends ClassVisitor {

	private ClassWriter writer;

	public HackCommandsWriter(ClassWriter writer) {
		super(Opcodes.ASM6, writer);
		this.writer = writer;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new HackMethod(writer.visitMethod(access, name, desc, signature, exceptions), name.equals("<init>"));
	}

	public class HackMethod extends MethodVisitor {

		private MethodVisitor defaultVisitor;
		private boolean is;

		public HackMethod(MethodVisitor defaultVisitor, boolean is) {
			super(Opcodes.ASM6, defaultVisitor);
			this.defaultVisitor = defaultVisitor;
			this.is = is;
		}

		@Override
		public void visitFieldInsn(int opcode, String owner, String name, String desc) {
			// Insert Point: Code Line 14
			// 0: aload_0
			// 1: invokespecial // Method java/lang/Object."<init>":()V
			// 4: aload_0
			// 5: new // class com/mojang/brigadier/CommandDispatcher
			// 8: dup
			// 9: invokespecial // Method
			// com/mojang/brigadier/CommandDispatcher."<init>":()V
			// 12: putfield // Field dispatcher:Lcom/mojang/brigadier/CommandDispatcher;
			super.visitFieldInsn(opcode, owner, name, desc);
			if (is) {
				is = false;
				next("DEVersionCommand");
				next("DEClassReplaceCommand");
				next("DEASMClassReplaceCommand");
				next("DEClassListCommand");
				next("DERecoverCommand");
				next("DEMakeASMCommand");
				next("DEInsertASMCommand");
			}
		}

		private void next(String command) {
			// Code Format:
			// aload_0
			// getfield dispatcher:Lcom/mojang/brigadier/CommandDispatcher;
			// invokestatic MethodXXX.register:(Lcom/mojang/brigadier/CommandDispatcher;)V
			defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			defaultVisitor.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/commands/Commands", "dispatcher",
					"Lcom/mojang/brigadier/CommandDispatcher;");
			defaultVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/github/nickid2018/dynamicex/commands/" + command,
					"register", "(Lcom/mojang/brigadier/CommandDispatcher;)V", false);
		}
	}
}
