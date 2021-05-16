package com.github.nickid2018.util;

import java.io.*;
import java.net.*;
import java.security.*;
import org.apache.commons.io.*;

public class SHA256Compute {

	private static final String[] strHex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E",
			"F" };

	public static String getSHA256(String path) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(IOUtils.toByteArray(new FileInputStream(path)));
			for (int i = 0; i < digest.length; i++) {
				int d = digest[i];
				if (d < 0)
					d += 256;
				sb.append(strHex[d / 16] + strHex[d % 16]);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String getSHA256(InputStream path) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(IOUtils.toByteArray(path));
			for (int i = 0; i < digest.length; i++) {
				int d = digest[i];
				if (d < 0)
					d += 256;
				sb.append(strHex[d / 16] + strHex[d % 16]);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String getSHA256(URL path) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(IOUtils.toByteArray(path.openStream()));
			for (int i = 0; i < digest.length; i++) {
				int d = digest[i];
				if (d < 0)
					d += 256;
				sb.append(strHex[d / 16] + strHex[d % 16]);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
