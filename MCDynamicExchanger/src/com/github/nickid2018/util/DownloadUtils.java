package com.github.nickid2018.util;

import java.io.*;
import java.net.*;
import org.apache.commons.io.*;

public class DownloadUtils {

	public static boolean downloadResource(String url, String to) {
		try {
			URL urlFile = new URL(url);
			File file = new File(to);
			if (!file.exists())
				file.createNewFile();
			System.out.println("Downloading resource \"" + url + "\"");
			URLConnection connect = urlFile.openConnection();
			InputStream is = connect.getInputStream();
			Thread t = new Thread(() -> {
				try {
					OutputStream stream = new FileOutputStream(file);
					IOUtils.copy(is, stream);
					stream.close();
					System.out.println("Downloaded resource \"" + url + "\"");
				} catch (IOException e) {
					System.err.println("Cannot download resource \"" + url + "\"");
					e.printStackTrace();
				}
			});
			t.start();
			while (t.isAlive()) {
				Thread.sleep(2000);
				System.out.println("Downloading Progress: " + is.available() + "B to read, "
						+ connect.getContentLength() + "B in total");
			}
		} catch (Exception e) {
			System.err.println("Cannot download resource \"" + url + "\"");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
