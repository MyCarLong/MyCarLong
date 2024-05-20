package com.mycarlong.utils;


public class FileNameUtil {

	public static String sanitizeFileName(String input) {
		// 한글, 영어, 숫자만 허용하고, 나머지는 _ 로 치환
		String output = input.replaceAll("[^a-zA-Z0-9가-힣]", "_");

		// Windows에서 파일 이름에 사용할 수 없는 문자를 _ 로 치환
		String[] invalidChars = new String[] {"<", ">", ":", "\"", "/", "\\", "|", "?", "*"};
		for (String invalidChar : invalidChars) {
			output = output.replace(invalidChar, "_");
		}

		return output;
	}
}