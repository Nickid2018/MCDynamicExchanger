package com.github.nickid2018.compare;

import java.util.function.*;

public enum CompareResultType {

	NONE((f) -> null), //
	NEW_FILE((f) -> "New File: " + f), // New File
	DELETE_FILE((f) -> "Delete File: " + f), // Delete File
	MODIFY((f) -> "Modify File: " + f); // Modify File

	private Function<String, String> formatAction;

	private CompareResultType(Function<String, String> formatAction) {
		this.formatAction = formatAction;
	}

	String format(String s) {
		return formatAction.apply(s);
	}
}