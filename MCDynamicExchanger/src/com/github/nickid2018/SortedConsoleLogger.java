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
		System.err.println(I18N.getText("error.detail"));
		error.printStackTrace();
	}

	@Override
	public void flush() {
		lines.forEach(System.out::println);
		lines.clear();
	}

	@Override
	public void formattedInfo(String str) {
		info(I18N.getText(str));
	}

}
