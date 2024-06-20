package com.planner.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FriendInfoDTO {
	private Long member_my_id;				// 회원 (나의)시퀀스
	private Long member_friend_id;			// 회원 (친구)시퀀스
	private String friend_my_nickname;		// 나의 닉네임
	private String friend_nickname;			// 친구 닉네임
	private String friend_my_memo;			// 나의 메모
	private String friend_memo;				// 친구 메모
	private String friend_nickname_status;	// 친구 별명 상태 (받음 : R)
	private String friend_memo_status;		// 친구 메모 상태 (받음 : R)
}