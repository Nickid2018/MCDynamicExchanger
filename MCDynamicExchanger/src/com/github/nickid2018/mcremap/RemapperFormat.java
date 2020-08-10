package com.github.nickid2018.mcremap;

import java.util.*;

public abstract class RemapperFormat {

	protected Map<String, RemapClass> remaps = new HashMap<>();

	public abstract void processInitMap(String position) throws Exception;

	public abstract double getProcessInValue() throws Exception;
}
