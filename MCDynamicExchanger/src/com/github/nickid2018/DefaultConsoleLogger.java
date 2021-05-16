package com.github.nickid2018;

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

	@Override
	public void formattedInfo(String str) {
		info(I18N.getText(str));
	}

	@Override
	public void flush() {
	}

}
