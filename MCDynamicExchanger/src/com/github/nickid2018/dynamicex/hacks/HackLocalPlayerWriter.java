package com.github.nickid2018.dynamicex.hacks;

import net.minecraft.*;
import org.objectweb.asm.*;
import net.minecraft.client.*;
import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import net.minecraft.client.multiplayer.*;
import com.mojang.brigadier.exceptions.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.dynamicex.commands.*;

public class HackLocalPlayerWriter extends AbstractHackWriter {

	public HackLocalPlayerWriter(ClassWriter writer) {
		super(writer);
	}

	public static final boolean handleChat(String message) {
		if ((RunningEnvironment.runOnlyClientSide() && message.startsWith("/de")) || message.startsWith("/de:side")) {
			ClientPacketListener connection = Minecraft.getInstance().getConnection();
			if (connection != null) {
				CommandDispatcher<SharedSuggestionProvider> commandDispatcher = connection.getCommands();
				CommandSourceStack commandSource = Minecraft.getInstance().player.createCommandSourceStack();
				try {
					commandDispatcher.execute(message.substring(1), commandSource);
				} catch (CommandSyntaxException exception) {
					commandSource.sendFailure(ComponentUtils.fromMessage(exception.getRawMessage()));
					if (exception.getInput() == null || exception.getCursor() < 0)
						return false;
					TextComponent suggestion = (TextComponent) new TextComponent("").withStyle(ChatFormatting.GRAY)
							.withStyle(style -> style
									.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, message)));
					int textLength = Math.min(exception.getInput().length(), exception.getCursor());
					if (textLength > 10) {
						suggestion.append("...");
					}
					suggestion.append(exception.getInput().substring(Math.max(0, textLength - 10), textLength));
					if (textLength < exception.getInput().length()) {
						suggestion.append(new TextComponent(exception.getInput().substring(textLength))
								.withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.UNDERLINE }));
					}
					suggestion.append(new TranslatableComponent("command.context.here", new Object[0])
							.withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.ITALIC }));
					commandSource.sendFailure(suggestion);
				}
			}
		}
		return true;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.equals(ClassNameTransformer.getMethodName("net.minecraft.client.player.LocalPlayer",
				"chat(Ljava/lang/String;)V")) && desc.equals("(Ljava/lang/String;)V")) {
			MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/github/nickid2018/dynamicex/hacks/HackLocalPlayerWriter",
					"handleChat", "(Ljava/lang/String;)Z", false);
			Label l0 = new Label();
			mv.visitJumpInsn(Opcodes.IFEQ, l0);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD,
					ClassNameTransformer.getResourceName("net/minecraft/client/player/LocalPlayer"), "connection",
					"Lnet/minecraft/client/multiplayer/ClientPacketListener;");
			mv.visitTypeInsn(Opcodes.NEW,
					ClassNameTransformer.getResourceName("net/minecraft/network/protocol/game/ServerboundChatPacket"));
			mv.visitInsn(Opcodes.DUP);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
					ClassNameTransformer.getResourceName("net/minecraft/network/protocol/game/ServerboundChatPacket"),
					"<init>", "(Ljava/lang/String;)V", false);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
					ClassNameTransformer.getResourceName("net/minecraft/client/multiplayer/ClientPacketListener"),
					ClassNameTransformer.getMethodName("net.minecraft.client.multiplayer.ClientPacketListener",
							"send(Lnet/minecraft/network/protocol/Packet;)V"),
					"(" + ClassNameTransformer.getQualifiedName("net/minecraft/network/protocol/Packet") + ")V", false);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(4, 2);
			mv.visitEnd();
			return null;
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}
