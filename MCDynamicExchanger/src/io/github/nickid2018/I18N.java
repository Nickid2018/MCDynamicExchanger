package io.github.nickid2018;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

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
            LANG.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (IOException ignored) {
        }
    }
}
