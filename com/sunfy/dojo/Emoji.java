package com.sunfy.dojo;

import java.io.UnsupportedEncodingException;

public class Emoji {

	public static void main(String[] args) throws UnsupportedEncodingException {
		byte[] utf8 = {(byte) 0xF0, (byte) 0x9F, (byte) 0x98, 
				(byte) 0x84, (byte) 0xE4, (byte) 0xB8};
		String x = new String(utf8, "UTF-8");
		for (int i = 0; i < x.length(); i++) {
			System.out.println((int)x.charAt(i) + " : " + x.codePointAt(i) +" "+ judge(x.charAt(i)));
		}

	}
	
	private static String judge(char c) {
		if (Character.isHighSurrogate(c)) {
			return "HIGH";
		}
		
		if (Character.isLowSurrogate(c)) {
			return "LOW";
		}
		
		return "NORMAL";
	}

}
