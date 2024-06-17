package com.planner.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FriendDTO {
	private Long friend_id;			// 친구 시퀀스
	private Long member_id;			// 회원 (나의)시퀀스
	private Long member_send_id;	// 회원 (친구)시퀀스
	private String friend_name;		// 친구 별명
	private String friend_memo;		// 친구 메모
}