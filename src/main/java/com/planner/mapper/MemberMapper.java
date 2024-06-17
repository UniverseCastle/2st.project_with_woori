package com.planner.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.MemberDTO;

@Mapper
@Repository
public interface MemberMapper {
	
//	회원 아이디로 객체 가져오기
	public MemberDTO findByMember(String member_userid);
	public Optional<MemberDTO> findByUser(String member_userid);
	
//	아이디로 시퀀스 가져오기
	public Long findByMemberId(String member_userid);

//	회원가입
	public int memberInsert(MemberDTO memberDTO);
	
//	로그인
	public int memberLogin(@Param("member_userid") String member_userid,
						   @Param("member_password") String member_password);
	
//	모든유저 정보
	public List<MemberDTO> memberList(Long member_id);
	
//	친구신청 받는 아이디로 친구신청 상태 찾기
	public String findByMemberFriendStatus(@Param("member_receive_id") Long member_receive_id,
										   @Param("member_id") Long member_id);
	
//	친구신청 보낸 아이디 찾기
	public List<MemberDTO> findBySendId(Long member_id);
}