package io.github.nickid2018.util;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHACompute {

    private static final String[] strHex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E",
            "F"};

    public static String getSHA256(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(IOUtils.toByteArray(new FileInputStream(path)));
            for (int b : digest) {
                int d = b;
                if (d < 0)
                    d += 256;
                sb.append(strHex[d / 16]).append(strHex[d % 16]);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getSHA1(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(IOUtils.toByteArray(new FileInputStream(path)));
            for (int b : digest) {
                int d = b;
                if (d < 0)
                    d += 256;
                sb.append(strHex[d / 16]).append(strHex[d % 16]);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getSHA256(InputStream path) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(IOUtils.toByteArray(path));
            for (int b : digest) {
                int d = b;
                if (d < 0)
                    d += 256;
                sb.append(strHex[d / 16]).append(strHex[d % 16]);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getSHA256(URL path) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(IOUtils.toByteArray(path.openStream()));
            for (int b : digest) {
                int d = b;
                if (d < 0)
                    d += 256;
                sb.append(strHex[d / 16]).append(strHex[d % 16]);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
