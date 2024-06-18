package com.planner.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.planner.dto.MemberDTO;
import com.planner.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
//	회원 이메일로 객체 가져오기
	public MemberDTO findByMember(String member_email) {
		return memberMapper.findByMember(member_email);
	}
	public Optional<MemberDTO> findByUser(String member_email){
		return memberMapper.findByUser(member_email);
	}
	
//	회원 이메일로 시퀀스 불러오기
	public Long findByMemberId(String member_email) {
		return memberMapper.findByMemberId(member_email);
	}
	
//	회원가입
	public int memberInsert(MemberDTO memberDTO) {
		memberDTO.setMember_password(passwordEncoder.encode(memberDTO.getMember_password()));
		
		return memberMapper.memberInsert(memberDTO);
	}
	
//	로그인
	public int memberLogin(String member_email, String member_password) {
		return memberMapper.memberLogin(member_email, member_password);
	}
	
//	모든유저 정보
	public List<MemberDTO> memberList(Principal principal){
		Long myId = memberMapper.findByMemberId(principal.getName());
		List<MemberDTO> list = memberMapper.memberList(myId);
		
		List<MemberDTO> sendIdList = memberMapper.findBySendId(myId);
		
//		List<MemberDTO> friendList = memberMapper.friendList(myId);
		
//		System.out.println(list.size());
		list.removeAll(sendIdList);
//		System.out.println(list.size());
//		list.removeAll(friendList);
//		System.out.println(list.size());
		
		for (MemberDTO memberDTO : list) {												// 리스트에서 신청상태를 표시하기 위해 set
			String status = findByMemberFriendStatus(memberDTO.getMember_id(), myId);
			memberDTO.setFriend_request_status(status);
		}
		return list;
	}
	
//	친구신청 받는 아이디로 친구신청 상태 찾기
	public String findByMemberFriendStatus(Long member_receive_id, Long member_id) {	// member_receive_id : 친구신청 받는 아이디
		return memberMapper.findByMemberFriendStatus(member_receive_id, member_id);		// member_id : 친구신청 보낸 (나의) 아이디
	}
	
//	친구신청 보낸 아이디 찾기
	public List<MemberDTO> findBySendId(Long member_id) {
		return memberMapper.findBySendId(member_id);
	}
	
//	친구신청 받은 아이디로 보낸 아이디 찾기 (친구신청 받았을 때)
//	public List<FriendRequestDTO> findBySendIdList(Principal principal) {
//		Long myId = memberMapper.findByMemberId(principal.getName());
//		List<FriendRequestDTO> sendIdList = null;
//		
//		try {
//			friendMapper.findBySendIdList(myId);		// 나의(신청 받은) 아이디로 보낸아이디(들) 검색한 리스트
//			System.out.println("서비스 센드리스트"+sendIdList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sendIdList;
//	}
	
	
//	public List<MemberDTO> memberList(Principal principal) {
//		friendMapper.RequestStatus(null, null) 여기까지 하다 끔!
		
//		Long userid = memberMapper.findByMemberId(principal.getName());
//		
//		List<MemberDTO> list = memberMapper.memberList();
//		
//		for (MemberDTO memberDTO : list) {
//			
//			memberDTO.setMember_id(userid);
//		}
//		
//		return list;
//	}
}