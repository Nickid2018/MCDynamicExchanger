package com.github.nickid2018;

public class DefaultConsoleLogger implements ISystemLogger {

    public void info(String string) {
        System.out.println(string);
    }

    public void error(String string, Throwable error) {
        System.err.println(string);
        System.err.println(I18N.getText("error.detail"));
        error.printStackTrace();
    }

    public void formattedInfo(String str) {
        info(I18N.getText(str));
    }

    public void flush() {
    }

}
