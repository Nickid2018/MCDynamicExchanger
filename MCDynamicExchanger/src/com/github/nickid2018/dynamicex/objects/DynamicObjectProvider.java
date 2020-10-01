package com.github.nickid2018.dynamicex.objects;

import java.util.function.*;

public class DynamicObjectProvider<T> implements ObjectProvider<T> {

	private Supplier<T> source;

	public DynamicObjectProvider(Supplier<T> source) {
		this.source = source;
	}

	public void set(Supplier<T> source) {
		this.source = source;
	}

	@Override
	public T getObject() {
		return source.get();
	}

}
