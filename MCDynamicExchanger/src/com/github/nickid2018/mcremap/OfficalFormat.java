package com.github.nickid2018.mcremap;

import java.io.*;
import java.net.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.ProgramMain;
import com.github.nickid2018.argparser.*;

public class OfficalFormat extends RemapperFormat {

	private ByteArrayInputStream fileBuffer;
	private double all;

	public OfficalFormat(CommandResult result) {
		super(result);
	}

	@Override
	public void processInitMap(String position) throws Exception {
		URL url = new URL(position);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(url.openStream(), baos);
		fileBuffer = new ByteArrayInputStream(baos.toByteArray());
		BufferedReader token = new BufferedReader(new InputStreamReader(fileBuffer));
		token.mark(0);
		all = fileBuffer.available();
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
				remaps.put(toClass, new RemapClass(toClass, nowClass, this));
				revClass.put(nowClass, toClass);
				if (detail)
					ProgramMain.logger.info("Class Entry: " + toClass + " to " + nowClass);
			}
		}
		token.close();
		fileBuffer.reset();
		token = new BufferedReader(new InputStreamReader(fileBuffer));
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
					String source = nowTo.toString().trim();
					String to = descs[1].split("\\(")[0].trim();
					nowClazz.methodMappings.put(source, to);
					if (detail)
						ProgramMain.logger.info("Method Entry: " + source + " to " + to);
				} else {
					// Field
					String[] splits = now.trim().split(" -> ");
					String source = splits[1];
					String to = splits[0].split(" ")[1];
					nowClazz.fieldMappings.put(source + ClassUtils.mapToSig(splits[0].split(" ")[0], revClass), to);
					if (detail)
						ProgramMain.logger.info("Field Entry: " + source + " to " + to);
				}
			} else {
				// Class
				now = now.substring(0, now.length() - 1);
				String[] splits = now.split(" -> ");
				nowClass = splits[0];
				toClass = splits[1];
				nowClazz = remaps.get(toClass);
				if (detail)
					ProgramMain.logger.info("Now Class: " + toClass + " (" + nowClass + ")");
			}
		}
	}

	@Override
	public synchronized double getProcessInValue() throws Exception {
		if (fileBuffer == null)
			return 0;
		return 1 - ((double) fileBuffer.available()) / all;
	}
}
