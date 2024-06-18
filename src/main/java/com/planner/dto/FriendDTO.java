package com.planner.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FriendDTO {
	private Long friend_id;			// 친구 시퀀스
	private Long member_my_id;		// 회원 (나의)시퀀스
	private Long member_friend_id;	// 회원 (친구)시퀀스
	private String friend_name;		// 친구 별명
	private String friend_memo;		// 친구 메모
	private String friend_status;	// 친구 : F, 닉네임변경 : N
	private String member_userid;	// 별명 생성 전까지 보여줄 회원아이디 / DB에 없음
}