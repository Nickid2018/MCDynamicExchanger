package com.github.nickid2018.mcremap;

import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;

public class OfficalFormat extends RemapperFormat {

	private ByteArrayInputStream bais;
	private double all;

	private Map<String, String> revClass = new HashMap<>();

	@Override
	public void processInitMap(String position) throws Exception {
		URL url = new URL(position);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(url.openStream(), baos);
		bais = new ByteArrayInputStream(baos.toByteArray());
		BufferedReader token = new BufferedReader(new InputStreamReader(bais));
		token.mark(0);
		all = bais.available();
		String nowClass = null;
		String toClass = null;
		String nowStr;
		while ((nowStr = token.readLine()) != null) {
			if (nowStr.startsWith("#"))
				continue;
			String now = nowStr.trim();
			if (!nowStr.startsWith(" ")) {
				// Class
				now = now.substring(0, now.length() - 1);
				String[] splits = now.split(" -> ");
				nowClass = splits[0];
				toClass = splits[1];
				remaps.put(toClass, new RemapClass(toClass, nowClass));
				revClass.put(nowClass, toClass);
			}
		}
		token.close();
		bais.reset();
		token = new BufferedReader(new InputStreamReader(bais));
		RemapClass nowClazz = null;
		while ((nowStr = token.readLine()) != null) {
			if (nowStr.startsWith("#"))
				continue;
			String now = nowStr.trim();
			if (nowStr.startsWith(" ")) {
				if (now.indexOf('(') >= 0) {
					// Function
					String[] ssource = now.split(":", 3);
					String[] splits = ssource[ssource.length - 1].trim().split(" -> ");
					StringBuilder nowTo = new StringBuilder(splits[1] + "(");
					String[] descs = splits[0].split(" ");
					String[] argss = descs[1].split("[\\(\\)]");
					if (argss.length == 2) {
						String[] args = argss[1].split(",");
						for (String a : args) {
							nowTo.append(ClassUtils.mapToSig(a, revClass));
						}
					}
					nowTo.append(")");
					nowTo.append(ClassUtils.mapToSig(descs[0], revClass));
					nowClazz.methodMappings.put(nowTo.toString().trim(), descs[1].split("\\(")[0].trim());
				} else {
					// Field
					String[] splits = now.trim().split(" -> ");
					nowClazz.fieldMappings.put(splits[1], splits[0].split(" ")[1]);
				}
			} else {
				// Class
				now = now.substring(0, now.length() - 1);
				String[] splits = now.split(" -> ");
				nowClass = splits[0];
				toClass = splits[1];
				nowClazz = remaps.get(toClass);
			}
		}
	}

	@Override
	public synchronized double getProcessInValue() throws Exception {
		if (bais == null)
			return 0;
		return 1 - ((double) bais.available()) / all;
	}
}
