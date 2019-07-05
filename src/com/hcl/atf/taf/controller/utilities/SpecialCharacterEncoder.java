package com.hcl.atf.taf.controller.utilities;

public class SpecialCharacterEncoder {

	static String[][] reservedCharacters = {
		{":"," &1& "},
		{"#"," &2& "},
		{"["," &3& "},
		{"]"," &4& "},
		{";"," &5& "},
		{"\n"," &6& "},
		{"\r"," &7& "},
	}; 

	static int reservedCharactersCount = 7;
	public static String encodeReservedCharacters(String jsonContent) {
		
		if (jsonContent == null)
			return jsonContent;
		String encodedContent = jsonContent;
		for (int i = 0; i < reservedCharactersCount; i++){
			encodedContent = encodedContent.replace(reservedCharacters[i][0], reservedCharacters[i][1]);
		}
		return encodedContent;
	}
	
	public static String decodeReservedCharacters(String jsonContent) {
		
		if (jsonContent == null)
			return jsonContent;
		String encodedContent = jsonContent;
		for (int i = 0; i < reservedCharactersCount; i++){
			encodedContent = encodedContent.replace(reservedCharacters[i][1], reservedCharacters[i][0]);
		}
		return encodedContent;
	}
}
