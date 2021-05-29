package com.github.nickid2018.mcremap.optimize;

public enum LocalRenameMode {

	NONE, ERASE, VAR_COUNT, TYPE_COUNT;

	public static LocalRenameMode getMode(String type) {
		switch (type.toLowerCase()) {
		case "none":
			return NONE;
		case "erase":
			return ERASE;
		case "var_count":
			return VAR_COUNT;
		case "type_count":
			return TYPE_COUNT;
		default:
			throw new IllegalArgumentException("Unknown parameter replace mode - " + type);
		}
	}
}
