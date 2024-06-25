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
	
//	회원 이메일로 객체 가져오기
	public MemberDTO findByMember(String member_email);
	public Optional<MemberDTO> findByUser(String member_email);
	
//	회원 시퀀스로 객체 가져오기
	public MemberDTO findByMemberSeq(Long member_id);
	
//	회원 이메일로 시퀀스 가져오기
	public Long findByMemberId(String member_email);
	
//	회원 시퀀스로 이메일 찾기
	public String findByMemberEmail(Long member_id);
	
//	회원 시퀀스로 회원이름 찾기
	public String findByMemberName(Long member_id);

//	회원가입
	public int memberInsert(MemberDTO memberDTO);
	
//	로그인
	public int memberLogin(@Param("member_email") String member_email,
						   @Param("member_password") String member_password);
	
//	전체 회원 수
	public int memberCount(String keyword);
	
//	회원 검색
	public List<MemberDTO> memberSearch(@Param("member_id") Long member_id, @Param("keyword") String keyword,
									  @Param("start") int start, @Param("end") int end);
	
//	친구신청 받는 아이디로 친구신청 상태 찾기
//	public String findByMemberFriendStatus(@Param("member_receive_id") Long member_receive_id,
//										   @Param("member_id") Long member_id);
	
//	친구신청 보낸 아이디 찾기
	public List<MemberDTO> findBySendId(@Param("member_id") Long member_id, @Param("keyword") String keyword);
	
//	회원정보
	public MemberDTO memberInfo(String member_email);
	
//	친구목록
//	public List<MemberDTO> friendList(Long member_id);
}