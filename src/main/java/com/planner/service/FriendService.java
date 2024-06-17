package com.planner.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.FriendRequestDTO;
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
		
		String status = friendMapper.friendCheck(friendRequestDTO);		// 친구신청 상태 컬럼으로 중복 여부 판단
		if (status != null) {											// null : 신청 상태가 없음 = 요청/친구 아님
			throw new IllegalArgumentException();
		}else {
			friendMapper.friendRequest(friendRequestDTO);				// 친구신청 void 메서드
		}
	}
	
//	친구수락
	public void friendAccept(Long member_id, Principal principal) {
		FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
		Long myId = memberMapper.findByMemberId(principal.getName());	// 나의(보낸) 시퀀스
		
		friendRequestDTO.setMember_receive_id(member_id);				// 내가 친구신청 보낸 친구의 시퀀스
		friendRequestDTO.setMember_send_id(myId);
	}
	
//	(받은)친구신청 리스트
	public List<FriendRequestDTO> receiveRequestList(Principal principal) {
		Long myId = memberMapper.findByMemberId(principal.getName());
		List<FriendRequestDTO> list = friendMapper.receiveRequestList(myId);
		
		return list;
	}
	
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