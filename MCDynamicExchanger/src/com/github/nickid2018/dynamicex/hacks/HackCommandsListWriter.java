package com.github.nickid2018.dynamicex.hacks;

import org.objectweb.asm.*;
import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.client.multiplayer.*;
import com.github.nickid2018.dynamicex.*;
import net.minecraft.network.protocol.game.*;
import com.github.nickid2018.dynamicex.commands.de.*;

public class HackCommandsListWriter extends AbstractHackWriter {

	public HackCommandsListWriter(ClassWriter writer) {
		super(writer);
	}

	public static void doProcess(ClientGamePacketListener listener) {
		if (listener instanceof ClientPacketListener) {
			ClientPacketListener listen = (ClientPacketListener) listener;
			CommandDispatcher<SharedSuggestionProvider> dispatcher = listen.getCommands();
			DEVersionCommand.registerClient(dispatcher);
			DESideCommand.registerClient(dispatcher);
		}
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.equals(
				ClassNameTransformer.getMethodName("net.minecraft.network.protocol.game.ClientboundCommandsPacket",
						"handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V"))) {
			return new HackMethod(super.visitMethod(access, name, desc, signature, exceptions));
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	public class HackMethod extends MethodVisitor {

		private MethodVisitor defaultVisitor;

		public HackMethod(MethodVisitor defaultVisitor) {
			super(Opcodes.ASM6, defaultVisitor);
			this.defaultVisitor = defaultVisitor;
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == Opcodes.RETURN) {
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 1);
				defaultVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,
						"com/github/nickid2018/dynamicex/hacks/HackCommandsListWriter", "doProcess",
						"(" + ClassNameTransformer.getQualifiedName(
								"net/minecraft/network/protocol/game/ClientGamePacketListener") + ")V",
						false);
			}
			super.visitInsn(opcode);
		}
	}
}
