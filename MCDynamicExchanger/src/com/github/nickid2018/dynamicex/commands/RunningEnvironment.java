package com.github.nickid2018.dynamicex.commands;

public enum RunningEnvironment {

	CLIENT, SERVER, LOCAL;

	public static RunningEnvironment now = LOCAL;

	public static boolean runOnlyClientSide() {
		return now == CLIENT;
	}

	public static boolean runClientSide() {
		return now != SERVER;
	}

	public static boolean runServerSide() {
		return now == SERVER;
	}
}
