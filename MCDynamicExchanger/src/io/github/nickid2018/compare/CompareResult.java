package io.github.nickid2018.compare;

import io.github.nickid2018.util.SHACompute;

import java.io.InputStream;

public class CompareResult {

    public final CompareResultType type;
    public final String file;
    public String oldSHA256;
    public String newSHA256;

    public CompareResult(String file, InputStream oldV, InputStream newV) {
        this.file = file;
        if (oldV == null) {
            type = CompareResultType.NEW_FILE;
            newSHA256 = SHACompute.getSHA256(newV);
            return;
        }
        if (newV == null) {
            type = CompareResultType.DELETE_FILE;
            oldSHA256 = SHACompute.getSHA256(oldV);
            return;
        }
        oldSHA256 = SHACompute.getSHA256(oldV);
        newSHA256 = SHACompute.getSHA256(newV);
        type = oldSHA256.equals(newSHA256) ? CompareResultType.NONE : CompareResultType.MODIFY;
    }

    public String getMessage() {
        return type.format(file);
    }
}