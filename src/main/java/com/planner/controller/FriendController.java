package com.planner.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.FriendDTO;
import com.planner.dto.FriendRequestDTO;
import com.planner.service.FriendService;
import com.planner.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friend/*")
public class FriendController {

	private final FriendService friendService;
	private final MemberService memberService;
	
//	친구찾기 리스트 Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("findFriend")
	public String findFriend() {
		
		return "friend_findFriend";
	}
	
//	친구추가 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("friendRequest")
	@ResponseBody
	public String friendRequest(@RequestParam("member_id") Long member_id,
								Principal principal, Model model) {
		friendService.friendRequest(member_id, principal);			// 친구신청 void 메서드
		
		Long myid = memberService.findByMemberId(principal.getName());
		String friendStatus = friendService.friendRequestStatus(member_id, myid);	// 친구신청 상태 찾는 메서드 / (받는 아이디, 보낸 아이디)
		
		return friendStatus;
	}
	
//	(받은)친구신청 리스트 Get
	@GetMapping("receiveList")
	@PreAuthorize("isAuthenticated()")
	public String receiveList(Principal principal, Model model) {
		List<FriendRequestDTO> receiveList = friendService.receiveRequestList(principal);
		
		model.addAttribute("receiveList", receiveList);
		
		return "friend/friend_receive";
	}
	
//	친구수락 (친구상태 업데이트) Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("friendAccept")
//	@ResponseBody
	public String friendAccept(@RequestParam("member_send_id") Long member_send_id,
							   Principal principal, Model model) {
		friendService.friendAccept(principal, member_send_id);			// 요청 상태 업데이트 메서드
//		friendService.friendAdd(principal, member_send_id);				// 친구 테이블에 추가 메서드
		
//		Long myid = memberService.findByMemberId(principal.getName());
//		String friendStatus = memberService.findByMemberFriendStatus(member_id, myid);
		
		return "redirect:/friend/receiveList";
	}
	
//	(받은)친구신청 거절 Post
	@PostMapping("receiveDelete")
	@PreAuthorize("isAuthenticated()")
	public String receiveDelete(@RequestParam("member_send_id") Long member_send_id,
							    Principal principal) {
		friendService.receiveDelete(principal, member_send_id);
		
		return "redirect:/friend/receiveList";
	}
	
//	친구목록 Get
	@GetMapping("friendList")
	@PreAuthorize("isAuthenticated()")
	public String friendList(Principal principal, Model model) {
		List<FriendDTO> friendList = friendService.friendList(principal);
		model.addAttribute("friendList", friendList);
		
		return "friend/friend_friendList";
	}
	
//	친구 닉네임 변경 Post
	@PostMapping("friendName")
	@PreAuthorize("isAuthenticated()")
	public String friendName(FriendDTO friendDTO) {
		friendService.friendNickName(friendDTO);
		
		return "redirect:/friend/friendList";
	}
	
//	친구 메모 변경 Post
	@PostMapping("friendMemo")
	@PreAuthorize("isAuthenticated()")
	public String friendMemo(FriendDTO friendDTO) {
		friendService.friendMemo(friendDTO);
		
		return "redirect:/friend/friendList";
	}
	


}