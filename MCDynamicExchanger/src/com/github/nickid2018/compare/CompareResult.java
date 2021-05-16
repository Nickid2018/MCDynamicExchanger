package com.github.nickid2018.compare;

import java.io.*;
import com.github.nickid2018.util.*;

public class CompareResult {

	public CompareResultType type;
	public String file;
	public String oldSHA256;
	public String newSHA256;

	public CompareResult(String file, InputStream oldV, InputStream newV) {
		this.file = file;
		if (oldV == null) {
			type = CompareResultType.NEW_FILE;
			newSHA256 = SHA256Compute.getSHA256(newV);
			return;
		}
		if (newV == null) {
			type = CompareResultType.DELETE_FILE;
			oldSHA256 = SHA256Compute.getSHA256(oldV);
			return;
		}
		oldSHA256 = SHA256Compute.getSHA256(oldV);
		newSHA256 = SHA256Compute.getSHA256(newV);
		type = oldSHA256.equals(newSHA256) ? CompareResultType.NONE : CompareResultType.MODIFY;
	}

	public String getMessage() {
		return type.format(file);
	}
}