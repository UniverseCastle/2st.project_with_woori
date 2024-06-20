package com.planner.enums;

import lombok.Getter;

@Getter
public enum FriendRole {

	REQUEST("R", "요청중"),
	SUCCESS("F", "친구");
	
	private String code;
	private String name;
	
	FriendRole(String code, String name){
		this.code = code;
		this.name = name;
	}
	
	public static String fromString(String code) {
		for (FriendRole role : FriendRole.values()) {
			if (role.getCode().equals(code)) {
				return role.getName();
			}
		}
		return null;
	}
}

//public static String fromString(UserRole userRole) {
//for (UserRole role : UserRole.values()) {
//	if (role.equals(userRole)) {
//		return role.getName();
//	}
//}
//throw new IllegalArgumentException();
//}


//REQUEST("R", "요청중"),
//SUCCESS("S", "친구");
//
//private String code;
//private String name;

//for(UserRole userRole : UserRole.values()) {
//
//}