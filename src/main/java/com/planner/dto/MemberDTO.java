package com.planner.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MemberDTO {
	private Long member_id;					// 회원 시퀀스
	private String member_email;			// 이메일
	private String member_password;			// 비밀번호
	private String member_name;				// 이름
	private LocalDate member_birth;			// 생년월일
	private String member_phone;			// 전화번호
	private String member_gender;			// 성별		(남: M, 여: W)
	private LocalDateTime member_reg;		// 가입일시	(default: sysdate)
	private String member_status;			// 회원상태	(기본: b, 탈퇴: d, 정지: j)
	private String oauth_id;				// 소셜 로그인
	private String friend_request_status;	// 친구신청 상태 리스트 (요청 : R, 친구 : F)	/ DB에 없음
	
//	private int member_count;				// 리스트에 포함된 회원 수 (페이징 처리에 사용)/ DB에 없음
//	private Long member_receive_id;			// 친구신청 받은 아이디	/ DB에 없음
//	private Long member_send_id;			// 친구신청 보낸 아이디	/ DB에 없음
	
//	private List<FriendRequestDTO> friendRequestList;
	
//	private FriendRequestDTO frDTO;			// 친구신청 상태 객체
}