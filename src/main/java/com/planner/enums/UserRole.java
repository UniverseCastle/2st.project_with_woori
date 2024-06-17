package com.planner.enums;

import lombok.Getter;

@Getter
public enum UserRole {

	USER("일반회원"),
	ADMIN("관리자");
	
	private String name;
	
	UserRole(String name){
		this.name = name;
	}
}