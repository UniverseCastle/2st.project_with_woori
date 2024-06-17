package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.planner.dto.FriendRequestDTO;

@Mapper
@Repository
public interface FriendMapper {
	
//	아이디 받아서 객체 찾기
	public FriendRequestDTO findByFriendRequest(Long member_id);
	
//	아이디 받아서 (보낸)시퀀스 찾기 (principal 사용)
//	public Long findByMemberId(String member_name);
	
//	친구신청 (보냄)
	public void friendRequest(FriendRequestDTO friendRequestDTO);
	
//	친구신청 전 중복검사
	public String friendCheck(FriendRequestDTO friendRequestDTO);
	
//	친구신청 받은 아이디로 보낸 아이디 찾기
//	public List<FriendRequestDTO> findBySendIdList(Long member_receive_id);
	
//	친구수락
	public void friendAccept(FriendRequestDTO friendRequestDTO);
	
//	(받은)친구신청 리스트
	public List<FriendRequestDTO> receiveRequestList(Long member_receive_id);
	
//	친구신청 (받음)
//	public void friendReceive(FriendRequestDTO friendRequestDTO);
	
//	친구신청 상태
//	public String RequestStatus(String member_receive_id, String member_send_id);
	
}