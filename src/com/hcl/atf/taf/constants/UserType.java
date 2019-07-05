package com.hcl.atf.taf.constants;

public enum UserType {
	ROLE_ADMIN,ROLE_TESTER,ROLE_SYSTEM_TERMINAL;
	
	public static int getUserType(String type){
		if(type.equalsIgnoreCase(ROLE_ADMIN.toString())){
			return 1;
		}else if(type.equalsIgnoreCase(ROLE_TESTER.toString())){
			return 2;
		}else if(type.equalsIgnoreCase(ROLE_SYSTEM_TERMINAL.toString())){
			return 3;
		}else{
			return -1;
		}
	}
}
