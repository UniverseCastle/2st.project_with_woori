package com.planner.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.planner.dto.MemberDTO;
import com.planner.mapper.FriendMapper;
import com.planner.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final FriendMapper friendMapper;
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
	
//	전체 회원 수
	public int memberCount(String keyword) {
		return memberMapper.memberCount(keyword);
	}
	
//	모든유저 정보
	public List<MemberDTO> memberList(Principal principal, String keyword, int start, int end){
		Long myId = memberMapper.findByMemberId(principal.getName());
		List<MemberDTO> list = memberMapper.memberList(myId, keyword, start, end);
		List<MemberDTO> sendIdList = memberMapper.findBySendId(myId, keyword);
		
		if (!sendIdList.isEmpty()) {
			list.removeAll(sendIdList);			// 보낸사람 기준 여러명에게 보낸 만큼 중복되어 나오는 데이터 삭제
		}
		for (MemberDTO memberDTO : list) {		// 리스트에서 신청상태를 표시하기 위해 set
			String status = friendMapper.friendRequestStatus(memberDTO.getMember_id(), myId);
			memberDTO.setFriend_request_status(status);
		}
		return list;
	}
	
//	친구신청 보낸 아이디 찾기
	public List<MemberDTO> findBySendId(Principal principal, @Param("keyword") String keyword) {
		Long member_id = memberMapper.findByMemberId(principal.getName());
		return memberMapper.findBySendId(member_id, keyword);
	}
	
//	내 (회원)정보
	public MemberDTO myInfo(String member_email) {
		return memberMapper.myInfo(member_email);
	}
	
//	친구신청 받는 아이디로 친구신청 상태 찾기
//	public String findByMemberFriendStatus(Long member_receive_id, Long member_id) {	// member_receive_id : 친구신청 받는 아이디
//		return memberMapper.findByMemberFriendStatus(member_receive_id, member_id);		// member_id : 친구신청 보낸 (나의) 아이디
//	}
	
//	친구신청 보낸 아이디 찾기
//	public List<MemberDTO> findBySendId(Long member_id) {
//		return memberMapper.findBySendId(member_id);
//	}
	
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