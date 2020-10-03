package com.github.nickid2018.dynamicex.objects;

import java.util.*;

import com.github.nickid2018.dynamicex.ClassNameTransformer;

import net.minecraft.client.*;
import net.minecraft.client.server.*;

public class ObjectsSet {

	public static final ObjectsSet INSTANCE = new ObjectsSet();

	public Map<String, ObjectProvider<?>> objects = new HashMap<>();
	public Map<String, String> nameMap = new HashMap<>();

	private ObjectsSet() {
		init();
	}

	public boolean hasObject(String name) {
		return objects.containsKey(name);
	}

	public void loadToStack(String name) {
		ObjectRunningStack.INSTANCE.pushObject(objects.get(name).getObject());
	}

	public void loadReferenceToStack(String name) {
		ObjectRunningStack.INSTANCE.pushObject(objects.get(name));
	}

	public void saveFromStack(String name) {
		Object obj = ObjectRunningStack.INSTANCE.popObject();
		nameMap.put(obj.getClass().getTypeName(), ClassNameTransformer.findSourceName(obj.getClass().getTypeName()));
		objects.put(name, obj instanceof ObjectProvider ? (ObjectProvider<?>) obj : new StaticObjectProvider<>(obj));
	}

	public void init() {
		objects.put("minecraft", new DynamicObjectProvider<>(Minecraft::getInstance));
		objects.put("localPlayer", new DynamicObjectProvider<>(() -> Minecraft.getInstance().player));
		objects.put("singleServer", new DynamicObjectProvider<>(Minecraft.getInstance()::getSingleplayerServer));
		objects.put("serverPlayer", new DynamicObjectProvider<>(() -> {
			IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
			return server == null ? null : server.getPlayerList().getPlayerByName(server.getSingleplayerName());
		}));
		nameMap.put(ClassNameTransformer.getClassName("net.minecraft.client.Minecraft"),
				"net.minecraft.client.Minecraft");
		nameMap.put(ClassNameTransformer.getClassName(" net.minecraft.client.player.LocalPlayer"),
				" net.minecraft.client.player.LocalPlayer");
		nameMap.put(ClassNameTransformer.getClassName("net.minecraft.client.server.IntegratedServer"),
				"net.minecraft.client.server.IntegratedServer");
		nameMap.put(ClassNameTransformer.getClassName("net.minecraft.server.level.ServerPlayer"),
				"net.minecraft.server.level.ServerPlayer");
	}

	public void flushBase() {

	}
}
