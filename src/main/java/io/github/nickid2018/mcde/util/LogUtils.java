package io.github.nickid2018.mcde.util;

public class LogUtils {
    public static void log(String message, Object... args) {
        System.out.println(I18N.getTranslation(message, args));
    }

    public static void err(String message, Throwable throwable, Object... args) {
        System.err.println(I18N.getTranslation(message, args));
        if (throwable != null)
            throwable.printStackTrace();
    }
}
