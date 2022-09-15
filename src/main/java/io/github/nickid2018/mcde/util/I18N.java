package io.github.nickid2018.mcde.util;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public class I18N {

    private static final Properties translations = new Properties();

    public static String getTranslation(String key) {
        return (String) translations.get(key);
    }

    static {
        String lang = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
        translations.clear();
        try {
            translations.load(new InputStreamReader(
                    Objects.requireNonNull(I18N.class.getResourceAsStream("/lang/" + lang + ".txt")),
                    StandardCharsets.UTF_8));
        } catch (Exception e) {
            try {
                translations.load(new InputStreamReader(
                        Objects.requireNonNull(I18N.class.getResourceAsStream("/lang/en_us.txt")),
                        StandardCharsets.UTF_8));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
