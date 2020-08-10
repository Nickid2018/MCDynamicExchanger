package com.github.nickid2018.asmexecutor.gen;

import java.io.*;
import org.objectweb.asm.*;
import org.apache.commons.io.*;
import static org.objectweb.asm.Opcodes.*;

public class ClassTransformer extends ClassVisitor {

	private StringWriter writer = new StringWriter();
	private String clazzName;
	private byte[] asmcode;

	public ClassTransformer(String asmcode) throws FileNotFoundException, IOException {
		super(ASM6);
		this.asmcode = IOUtils.toByteArray(new FileInputStream(asmcode));
	}

	public ClassTransformer(byte[] asmcode) {
		super(ASM6);
		this.asmcode = asmcode;
	}

	public void execute() throws IOException {
		ClassReader reader = new ClassReader(asmcode);
		reader.accept(this, ClassReader.SKIP_DEBUG);
		String[] sps = clazzName.split("/");
		File file = new File(sps[sps.length - 1] + ".asm");
		if (!file.exists())
			file.createNewFile();
		this.writer.flush();
		FileWriter writer = new FileWriter(file);
		writer.write(this.writer.toString().replaceAll(clazzName, "THIS_CLASS"));
		writer.close();
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		StringBuilder sb = new StringBuilder(access + " " + superName + " ");
		if (interfaces.length > 0) {
			for (String itf : interfaces) {
				sb.append(itf + " ");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("\n");
		try {
			IOUtils.write(sb, writer);
		} catch (IOException e) {
		}
		clazzName = name;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		try {
			IOUtils.write("METHOD " + access + " " + name + " " + desc + "\n", writer);
			return new ClassMethodWriter(desc, name, clazzName, writer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object cst) {
		try {
			String add = "";
			if (cst instanceof String)
				add = " STRING " + ((String) cst).replaceAll("\n", "\\\\NEWLINE\\\\");
			if (cst instanceof Type)
				add = " CLASS " + ((Type) cst).getInternalName();
			if (cst instanceof Integer)
				add = " INTEGER " + cst;
			if (cst instanceof Float)
				add = " FLOAT " + cst;
			if (cst instanceof Long)
				add = " LONG " + cst;
			if (cst instanceof Double)
				add = " DOUBLE " + cst;
			IOUtils.write("FIELD " + access + " " + name + " " + desc + add + "\n", writer);
			return super.visitField(access, name, desc, signature, cst);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static class ClassMethodWriter extends MethodTransformer.MethodWriter {

		public ClassMethodWriter(String desc, String name, String clazzName, StringWriter writer) throws IOException {
			super(desc, name, clazzName, false);
			this.writer = writer;
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			try {
				IOUtils.write(
						MethodTransformer.OPCODES.get(opcode) + " " + (owner.equals(clazzName) ? "THIS_CLASS" : owner)
								+ " " + name + " " + desc + " " + (itf ? "TRUE" : "FALSE") + "\n",
						writer);
			} catch (IOException e) {
			}
		}
	}
}
