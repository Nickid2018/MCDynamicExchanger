package com.github.nickid2018;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class I18N {

	public static final Properties LANG = new Properties();
	public static String NOW;

	public static String getText(String key, Object... args) {
		return String.format(LANG.getProperty(key, key), args);
	}

	public static void init(String lang) {
		NOW = lang == null ? Locale.getDefault().toString() : lang;
		InputStream is = I18N.class.getResourceAsStream("/assets/lang/" + NOW + ".lang");
		if (is == null)
			is = I18N.class.getResourceAsStream("/assets/lang/en_US.lang");
		try {
			LANG.load(new InputStreamReader(is, Charset.forName("utf-8")));
		} catch (IOException e) {
		}
	}
}
