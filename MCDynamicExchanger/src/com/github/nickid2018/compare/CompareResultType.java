package com.github.nickid2018.compare;

import com.github.nickid2018.I18N;

import java.util.function.Function;

public enum CompareResultType {

    NONE(f -> null), //
    NEW_FILE(f -> I18N.getText("compare.newfile", f)), // New File
    DELETE_FILE(f -> I18N.getText("compare.delete", f)), // Delete File
    MODIFY(f -> I18N.getText("compare.modify", f)); // Modify File

    private final Function<String, String> formatAction;

    CompareResultType(Function<String, String> formatAction) {
        this.formatAction = formatAction;
    }

    String format(String s) {
        return formatAction.apply(s);
    }
}