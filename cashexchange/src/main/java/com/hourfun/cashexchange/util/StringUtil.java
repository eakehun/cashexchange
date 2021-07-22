package com.hourfun.cashexchange.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StringUtil {
	/*
	 * 그냥 검색해서 찾은 마스킹 코드
	 */
	public static String getMaskedEmail(String email) {

		String regex = "\\b(\\S+)+@(\\S+.\\S+)";
		Matcher matcher = Pattern.compile(regex).matcher(email);
		if (matcher.find()) {
			String id = matcher.group(1);
			int length = id.length();
			if (length < 3) {
				char[] c = new char[length];
				Arrays.fill(c, '*');
				return email.replace(id, String.valueOf(c));
			} else if (length == 3) {
				return email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
			} else {
				return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
			}
		}
		return email;
	}
	
	public static String encryptAES256(String msg, String key) throws Exception {

		SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

		// CBC : Cipher Block Chaining Mode

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, secret);

		byte[] encrypted = cipher.doFinal(msg.getBytes());
		return byteToBase64String(encrypted);

	}

	public static String sha256(String msg)  throws NoSuchAlgorithmException {

	    MessageDigest md = MessageDigest.getInstance("SHA-256");

	    md.update(msg.getBytes());

	    return byteToHexString(md.digest());

	}
	
	public static String byteToHexString(byte[] data) {

	    StringBuilder sb = new StringBuilder();

	    for(byte b : data) {

	        sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

	    }

	    return sb.toString();

	}
	
	public static String byteToBase64String(byte[] data) {
		Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(data);
		
		return new String(encodedBytes);
	}
}
