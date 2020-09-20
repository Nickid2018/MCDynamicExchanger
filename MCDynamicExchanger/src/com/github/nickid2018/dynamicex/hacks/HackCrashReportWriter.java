package com.github.nickid2018.dynamicex.hacks;

import java.util.*;
import net.minecraft.*;
import org.objectweb.asm.*;
import com.github.nickid2018.dynamicex.*;

public class HackCrashReportWriter extends AbstractHackWriter {

	public HackCrashReportWriter(ClassWriter writer) {
		super(writer);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new HackMethod(writer.visitMethod(access, name, desc, signature, exceptions), name.equals("<init>"));
	}

	public static final void hackInCrashReport(CrashReport report) {
		String lineSeparator = System.getProperty("line.separator");
		CrashReportCategory category = report.addCategory("Classes Redefined");
		for (Map.Entry<String, DynamicClass> en : DynamicClassesHolder.list().entrySet()) {
			DynamicClass clazz = en.getValue();
			StringBuilder builder = new StringBuilder("Data Path:" + clazz.filePath + lineSeparator + "\t\t");
			byte[] bytes = clazz.definition.getDefinitionClassFile();
			int now = 0;
			for (byte b : bytes) {
				now++;
				String hex = Integer.toHexString(((int) b) & 0xFF).toUpperCase();
				if (hex.length() == 1)
					builder.append("0" + hex + " ");
				else
					builder.append(hex + " ");
				if (now == 200) {
					now = 0;
					builder.append(lineSeparator + "\t\t");
				}
			}
			category.setDetail("Class " + en.getKey(), builder);
		}
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
		public void visitInsn(int opcode) {
			// Hack Point on RETURN
			if (is && opcode == Opcodes.RETURN) {
				// Redircect to this function..
				// aload_0 [this]
				// invokestatic com/github/nickid2018/dynamicex/hacks/HackCrashReportWriter
				// hackInCrashReport (Lnet/minecraft/CrashReport;)V
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);
				defaultVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
						"com/github/nickid2018/dynamicex/hacks/HackCrashReportWriter", "hackInCrashReport",
						"(Lnet/minecraft/CrashReport;)V", false);
			}
			super.visitInsn(opcode);
		}
	}
}
