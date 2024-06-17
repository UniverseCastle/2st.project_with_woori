package com.planner.util;

import org.springframework.stereotype.Component;

import com.planner.enums.FriendRole;

@Component
public class FriendRoleUtils {
	public static String getFriendRoleName(String code) {
		return FriendRole.fromString(code);
	}
}