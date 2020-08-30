package com.github.nickid2018.util;

import org.objectweb.asm.*;

public class ClassStringReplacer extends ClassWriter {

	private String regex;
	private String to;

	public ClassStringReplacer(String regex, String to) {
		super(0);
		this.regex = regex;
		this.to = to;
	}

	@Override
	public int newUTF8(String value) {
		if (value.matches(regex))
			return super.newUTF8(to);
		return super.newUTF8(value);
	}
}
