package com.github.nickid2018.dynamicex.objects;

import java.util.*;
import java.lang.reflect.*;
import com.github.nickid2018.dynamicex.*;

public class ObjectOnlyElement extends ObjectElement {

	private Class<?> clazz;
	private ObjectProvider<?> listener;
	private Set<Field> fields = new HashSet<>();
	private Map<String, String> infos = new HashMap<>();

	public ObjectOnlyElement(Class<?> clazz, ObjectProvider<?> listener) throws ClassNotFoundException {
		this.clazz =clazz;
		this.listener = listener;
	}

	public void putNewField(String fieldName) throws Exception {
		Field field = null;
		Class<?> now = clazz;
		while (now != null) {
			try {
				field = now.getDeclaredField(fieldName);
				if (field != null)
					break;
			} catch (NoSuchFieldException e) {
				now = now.getSuperclass();
			}
		}
		if (field == null)
			throw new NoSuchFieldException(fieldName + " is not declared in this class.");
		field.setAccessible(true);
		fields.add(field);
	}

	private long lastErrorTime;

	@Override
	public boolean putInformation(Object from, String name, Object value) {
		if (listener.getObject() != from)
			return false;
		fields.forEach(field -> {
			String n = field.getName();
			try {
				Object object = field.get(value);
				infos.put(nameAlias.getOrDefault(n, n), object == null ? "null" : object.toString());
			} catch (Exception e) {
				if (System.currentTimeMillis() - lastErrorTime > 2000) {
					lastErrorTime = System.currentTimeMillis();
					SharedAfterLoadConstants.logger.error("Error in getting value " + name, e);
				}
			}
		});
		return true;
	}

	@Override
	public Map<String, String> getFormattedInformation() {
		return infos;
	}

}
