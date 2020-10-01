package com.github.nickid2018.dynamicex.objects;

public class StaticObjectProvider<T> implements ObjectProvider<T> {

	private T object;

	public StaticObjectProvider(T object) {
		this.object = object;
	}

	public void set(T obj) {
		object = obj;
	}

	@Override
	public T getObject() {
		return object;
	}

}
