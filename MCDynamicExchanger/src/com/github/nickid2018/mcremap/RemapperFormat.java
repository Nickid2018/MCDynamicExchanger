package com.github.nickid2018.mcremap;

import java.util.*;
import com.github.nickid2018.mcremap.argparser.*;

public abstract class RemapperFormat {

	public Map<String, RemapClass> remaps = new HashMap<>();
	public Map<String, String> revClass = new HashMap<>();

	protected boolean detail;

	public RemapperFormat(CommandResult result) {
		detail = result.containsSwitch("-D");
	}

	public abstract void processInitMap(String position) throws Exception;

	public abstract double getProcessInValue() throws Exception;
}
