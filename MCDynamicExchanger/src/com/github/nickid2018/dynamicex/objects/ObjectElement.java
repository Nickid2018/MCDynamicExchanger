package com.github.nickid2018.dynamicex.objects;

import java.util.*;

public abstract class ObjectElement {

	// Render Position
	public int x0;
	public int y0;

	// Scale
	public float scale;
	
	public Map<String,String> nameAlias = new HashMap<>();

	public abstract boolean putInformation(Object from, String name, Object value);
	
	public abstract Map<String,String> getFormattedInformation();
}
