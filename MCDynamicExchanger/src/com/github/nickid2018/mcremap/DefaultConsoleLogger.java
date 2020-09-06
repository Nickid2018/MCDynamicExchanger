package com.github.nickid2018.mcremap;

public class DefaultConsoleLogger implements ISystemLogger {

	@Override
	public void info(String string) {
		System.out.println(string);
	}

	@Override
	public void error(String string, Throwable error) {
		System.err.println(string);
		System.err.println("Details:");
		error.printStackTrace();
	}

}
