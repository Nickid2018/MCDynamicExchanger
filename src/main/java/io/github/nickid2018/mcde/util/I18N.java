package io.github.nickid2018.mcde.util;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public class I18N {

    private static final Properties translations = new Properties();

    public static String getTranslation(String key, Object... args) {
        return ((String) translations.getOrDefault(key, key)).formatted(args);
    }

    static {
        String lang = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
        lang = lang.toLowerCase(Locale.ROOT);
        System.out.println("Loading language: " + lang);
        translations.clear();
        try {
            translations.load(new InputStreamReader(
                    Objects.requireNonNull(I18N.class.getResourceAsStream("/lang/" + lang + ".txt")),
                    StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("Cannot load default language file! Now using English(en_us).");
            e.printStackTrace();
            try {
                translations.load(new InputStreamReader(
                        Objects.requireNonNull(I18N.class.getResourceAsStream("/lang/en_us.txt")),
                        StandardCharsets.UTF_8));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
