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
	
//	친구추가 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("friendRequest")
	@ResponseBody
	public String friendRequest(@RequestParam("member_id") Long member_id,
								Principal principal, Model model) {
		friendService.friendRequest(member_id, principal);			// 친구신청 void 메서드
		
		Long myid = memberService.findByMemberId(principal.getName());
		String friendStatus = memberService.findByMemberFriendStatus(member_id, myid);
		
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
	
//	(받은)친구신청 리스트 Post
	@PostMapping("receiveList")
	public String receiveList() {
		
		return "";
	}
	
//	(받은)친구신청 거절 Post
	@PostMapping("receiveDelete")
	@PreAuthorize("isAuthenticated()")
	public String receiveDelete(Long member_receive_id) {
		friendService.receiveDelete(null, null);
	}
	
//	친구목록 Get
	@GetMapping("friendList")
	@PreAuthorize("isAuthenticated()")
	public String friendList() {
		
		return "friend/friend_friendList";
	}
	
	/*
//	친구수락 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("friendAccept")
//	@ResponseBody
	public String friendAccept(@RequestParam("member_id") Long member_id,
								Principal principal, Model model) {
		friendService.friendAccept(member_id, principal);			// 친구수락 void 메서드
		
//		Long myid = memberService.findByMemberId(principal.getName());
//		String friendStatus = memberService.findByMemberFriendStatus(member_id, myid);
		
		return "redirect:/member/member_userInfo";
	}
	*/
}