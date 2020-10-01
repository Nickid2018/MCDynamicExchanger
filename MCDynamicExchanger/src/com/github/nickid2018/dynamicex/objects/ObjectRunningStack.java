package com.github.nickid2018.dynamicex.objects;

import java.util.*;

public class ObjectRunningStack {

	public static final ObjectRunningStack INSTANCE = new ObjectRunningStack();

	public Stack<Object> runningStack = new Stack<>();

	private ObjectRunningStack() {
	}

	public void pushObject(Object obj) {
		runningStack.push(obj);
	}

	public Object popObject() {
		return runningStack.pop();
	}

	public void dupicate() {
		runningStack.push(runningStack.peek());
	}
}
