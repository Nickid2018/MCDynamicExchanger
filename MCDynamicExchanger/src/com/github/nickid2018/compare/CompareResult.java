package com.github.nickid2018.compare;

import java.io.*;
import com.github.nickid2018.util.*;

public class CompareResult {

	public CompareResultType type;
	public String file;

	public CompareResult(String file, InputStream oldV, InputStream newV) {
		this.file = file;
		if (oldV == null) {
			type = CompareResultType.NEW_FILE;
			return;
		}
		if (newV == null) {
			type = CompareResultType.DELETE_FILE;
			return;
		}
		String oldMD5 = MD5Compute.getMD5(oldV);
		String newMD5 = MD5Compute.getMD5(newV);
		type = oldMD5.equals(newMD5) ? CompareResultType.NONE : CompareResultType.MODIFY;
	}

	public String getMessage() {
		return type.format(file);
	}
}