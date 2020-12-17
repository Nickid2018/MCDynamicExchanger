package com.github.nickid2018;

import java.util.*;

public class SortedConsoleLogger implements ISystemLogger {
	
	private Set<String> lines = new TreeSet<>();

	@Override
	public void info(String string) {
		lines.add(string);
	}

	@Override
	public void error(String string, Throwable error) {
		flush();
		System.err.println(string);
		System.err.println("Details:");
		error.printStackTrace();
	}

	@Override
	public void flush() {
		lines.forEach(System.out::println);
		lines.clear();
	}

	
}
