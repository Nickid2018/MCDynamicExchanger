package com.github.nickid2018.dynamicex.objects;

import java.util.*;
import net.minecraft.client.*;

public class ObjectsSet {

	public static final ObjectsSet INSTANCE = new ObjectsSet();

	public volatile Map<String, ObjectProvider<?>> objects = new HashMap<>();

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
		objects.put(name, obj instanceof ObjectProvider ? (ObjectProvider<?>) obj : new StaticObjectProvider<>(obj));
	}

	public void init() {
		objects.put("minecraft", new DynamicObjectProvider<>(Minecraft::getInstance));
		objects.put("localPlayer", new DynamicObjectProvider<>(() -> Minecraft.getInstance().player));
		objects.put("singleServer", new DynamicObjectProvider<>(Minecraft.getInstance()::getSingleplayerServer));
		objects.put("serverPlayer",
				new DynamicObjectProvider<>(() -> Minecraft.getInstance().getSingleplayerServer().getPlayerList()
						.getPlayerByName(Minecraft.getInstance().getSingleplayerServer().getSingleplayerName())));
	}

	public void flushBase() {

	}
}
