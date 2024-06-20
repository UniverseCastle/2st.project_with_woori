package com.planner.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.FriendDTO;
<<<<<<< HEAD
import com.planner.dto.FriendInfoDTO;
=======
>>>>>>> f3b7617 (friend_nickname)
import com.planner.dto.FriendRequestDTO;
import com.planner.dto.MemberDTO;
import com.planner.mapper.FriendMapper;
import com.planner.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

	private final FriendMapper friendMapper;
	private final MemberMapper memberMapper;
	
//	시퀀스 받아서 객체 찾기
	public FriendRequestDTO findByFriendRequest(Long member_id) {
		return friendMapper.findByFriendRequest(member_id);
	}
	
//	친구신청 (보냄)
	public void friendRequest(Long member_id, Principal principal) {	// member_id : 친구(신청 받은) 시퀀스
		FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
		Long myId = memberMapper.findByMemberId(principal.getName());	// 나의(보낸) 시퀀스
		
		friendRequestDTO.setMember_receive_id(member_id);				// 내가 친구신청 보낸 친구의 시퀀스
		friendRequestDTO.setMember_send_id(myId);						// 나의 시퀀스
		
//		String status = friendMapper.friendCheck(friendRequestDTO);		// 친구신청 상태 컬럼으로 중복 여부 판단
//		if (status != null) {											// null : 신청 상태가 없음 = 요청/친구 아님
//			throw new IllegalArgumentException();
//		}else {
			friendMapper.friendRequest(friendRequestDTO);				// 친구신청 void 메서드
//		}
	}
	
//	(받은)친구신청 리스트
	public List<FriendRequestDTO> receiveRequestList(Principal principal) {
		Long myId = memberMapper.findByMemberId(principal.getName());
		List<FriendRequestDTO> list = friendMapper.receiveRequestList(myId);
		
//		MemberDTO friendRequestDTO = memberMapper.findByMember(principal.getName());
		for (FriendRequestDTO friendRequestDTO : list) {
			if (friendRequestDTO.getMember_send_id() != null) {
				String email = memberMapper.findByEmail(friendRequestDTO.getMember_send_id());
				friendRequestDTO.setMember_email(email);
			}
		}
		
		return list;
	}
	
//	(받은)친구신청 거절
	public void receiveDelete(Principal principal,
			Long member_send_id) {
		Long myId = memberMapper.findByMemberId(principal.getName());
		friendMapper.receiveDelete(myId, member_send_id);
	}
	
//	친구수락 (+친구상태 업데이트)
	public void friendAccept(Principal principal, Long member_send_id) {
		Long myId = memberMapper.findByMemberId(principal.getName());		// 나의(받은) 시퀀스
		MemberDTO memberDTO = memberMapper.findByMemberSeq(member_send_id);
		FriendDTO friendDTO = new FriendDTO();
//		FriendInfoDTO friendInfoDTO = new FriendInfoDTO();
		
		friendDTO.setMember_my_id(myId);
		friendDTO.setMember_friend_id(member_send_id);
		friendDTO.setFriend_nickname(memberDTO.getMember_name());
		
//		friendInfoDTO.setMember_my_id(myId);
//		friendInfoDTO.setMember_friend_id(member_send_id);
//		friendInfoDTO.setMember_userid(memberDTO.getMember_userid());
		
		friendMapper.friendAccept(myId, member_send_id);				// 친구 상태 업데이트 메서드
		friendMapper.friendAdd(friendDTO);								// 친구 테이블에 추가 메서드
//		friendMapper.friendInfoAdd(friendInfoDTO);						// 친구 정보 테이블에 추가 메서드
		
//		friendRequestDTO.setMember_receive_id(member_id);				// 내가 친구신청 보낸 친구의 시퀀스
//		friendRequestDTO.setMember_send_id(myId);
	}
	
//	친구목록
	public List<FriendDTO> friendList(Principal principal) {
		long myId = memberMapper.findByMemberId(principal.getName());
		
		List<FriendDTO> list = friendMapper.friendList(myId);
		
		return list;
	}
	
<<<<<<< HEAD
//	친구 닉네임 변경 (추가)
	public void friendNickNameAdd(Long member_friend_id, String member_name, Principal principal) {
		Long myId = memberMapper.findByMemberId(principal.getName());
		
		FriendInfoDTO friendInfoDTO = new FriendInfoDTO();
		friendInfoDTO.setMember_my_id(myId);
		friendInfoDTO.setMember_friend_id(member_friend_id);
		friendInfoDTO.setFriend_nickname(member_name);			// 친구 별칭	/ member_name : DB에 없음
		
		friendMapper.friendNickNameAdd(friendInfoDTO);			// 닉네임 변경(추가) 메서드
	}
	
//	친구 닉네임 변경 (수정)
	public void friendNickNameUpdate(String friend_nickname, Long member_my_id) {
=======
//	친구 닉네임 변경
	public void friendNickNameSend(Long member_friend_id, String member_name, Principal principal) {
		Long myId = memberMapper.findByMemberId(principal.getName());
		
		FriendDTO friendDTO = new FriendDTO();
		friendDTO.setMember_my_id(myId);
		friendDTO.setMember_friend_id(member_friend_id);
		friendDTO.setFriend_nickname(member_name);			// 친구 별칭	/ member_name : DB에 없음
		
		friendMapper.friendNickNameSend(friendDTO);				// 닉네임 변경(추가) 메서드
	}
	
//	친구 닉네임 변경 ()
	public void friendNickNameReceive(String friend_nickname, Long member_my_id) {
>>>>>>> f3b7617 (friend_nickname)
		
	}

//	친구 테이블에 추가
//	public void friendAdd(Principal principal, Long member_send_id) {	
//	}
	
//	친구신청 보낸 아이디 찾기
//	public List<FriendRequestDTO> findBySendId(Long member_receive_id) {
//		return friendMapper.findBySendId(member_receive_id);
//	}
	
//	친구신청 받은 아이디로 보낸 아이디 찾기
//	public List<FriendRequestDTO> findBySendIdList(Long member_receive_id) {
//		
//		return friendMapper.findBySendIdList(member_receive_id);
//	}
	
//	친구신청 (받음)
//	public void friendReceive(FriendRequestDTO friendRequestDTO) {
//		friendMapper.friendReceive(friendRequestDTO);
//	}
	
//	친구신청 상태
//	public String RequestStatus(String member_receive_id, String member_send_id) {
//		return friendMapper.RequestStatus(member_receive_id, member_send_id);
//	}
}