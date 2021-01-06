package com.github.nickid2018;

public interface ISystemLogger {

	void info(String string);

	void error(String string, Throwable error);

	void flush();
}
