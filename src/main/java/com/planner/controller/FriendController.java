package com.planner.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.friend.FriendDTO;
import com.planner.dto.request.friend.FriendRequestDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.service.FriendService;
import com.planner.service.MemberService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

	private final FriendService friendService;
	private final MemberService memberService;
	
//	친구추가 Post / 친구목록 에서 ajax 서밋한 경우
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/request")
	@ResponseBody
	public String friendRequest(@RequestParam("member_id") Long member_id,
								@UserData ResMemberDetail dtail, Model model) {
		friendService.friendRequest(member_id, dtail);									// 친구신청 void 메서드
		
		Long myid = memberService.findByMemberId(dtail.getMember_email());
		String friendStatus = friendService.friendRequestStatus(member_id, myid);		// 친구신청 상태 찾는 메서드 / (받는 아이디, 보낸 아이디)
		
		return friendStatus;
	}
	
//	친구추가 Post / 회원정보 에서 submit 한 경우
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/requestByInfo")
	public String friendRequest(@RequestParam(value = "member_id") Long member_id, 
								@RequestParam(name = "delete_who", defaultValue = "none") String delete_who, 
								@RequestParam(value = "friend_request_status") String friend_request_status,
								@UserData ResMemberDetail detail, Model model) {
		if (friend_request_status.equals("search")) {
			friendService.friendRequest(member_id, detail);		// 친구신청 void 메서드
		}else if (friend_request_status.equals("receive")) {
			if (delete_who.equals("send")) {					// 받은 신청목록에서 온 경우
				friendService.requestDelete(detail.getMember_id(), member_id);	// 거절 메서드
				
				return String.format("redirect:/member/auth/info/%d", member_id);
			}
			friendService.friendAccept(detail, member_id);		// 수락 메서드
			
		}else if (friend_request_status.equals("send")) {
			if (delete_who.equals("receive")) {					// 보낸 신청목록에서 온 경우
				friendService.requestDelete(member_id, detail.getMember_id());
				
				return String.format("redirect:/member/auth/info/%d", member_id);
			}
		}
		
		
//		Long myid = memberService.findByMemberId(dtail.getMember_email());
//		String friendStatus = friendService.friendRequestStatus(member_id, myid);		// 친구신청 상태 찾는 메서드 / (받는 아이디, 보낸 아이디)
//		rttr.addFlashAttribute("friendStatus", friendStatus);							// 회원정보에서 버튼이 보여질 조건을 주기위한 변수
		
		return String.format("redirect:/member/auth/info/%d", member_id);
	}
	
//	(받은)친구신청 리스트 Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/receiveList")
	public String receiveList(@UserData ResMemberDetail dtail, Model model) {
		List<FriendRequestDTO> receiveList = friendService.receiveRequestList(dtail.getMember_email());
		int receive_count = friendService.receiveRequestCount(dtail.getMember_email());	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		model.addAttribute("receiveList", receiveList);
		
		return "friend/friend_receive";
	}
	
//	(보낸)친구신청 리스트 Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/sendList")
	public String sendList(@UserData ResMemberDetail dtail, Model model) {
		List<FriendRequestDTO> sendList = friendService.sendRequestList(dtail.getMember_email());
		int receive_count = friendService.receiveRequestCount(dtail.getMember_email());	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		model.addAttribute("sendList", sendList);
		
		return "friend/friend_send";
	}
	
//	친구수락 (친구상태 업데이트) Post / 받은요청 목록에서 수락 할 경우
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/accept")
	public String friendAccept(@RequestParam("member_send_id") Long member_send_id,
							   @UserData ResMemberDetail detail, Model model) {
		friendService.friendAccept(detail, member_send_id);			// 요청 상태 업데이트 메서드
		
		return "redirect:/friend/receiveList";
	}
	
//	친구신청 취소/거절 Post / 보낸요청 목록에서 취소/거절 한 경우
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/requestDelete")
	public String requestDelete(@RequestParam(name = "delete_who", defaultValue = "none") String delete_who,
								FriendRequestDTO friendRequestDTO, @UserData ResMemberDetail detail) {
		if (delete_who.equals("send")) {			// 받은 신청목록에서 온 경우
			friendService.requestDelete(detail.getMember_id(), friendRequestDTO.getMember_send_id());
		}else if (delete_who.equals("receive")) {	// 보낸 신청목록에서 온 경우
			friendService.requestDelete(friendRequestDTO.getMember_receive_id(), detail.getMember_id());
			
			return "redirect:/friend/sendList";
		}
		return "redirect:/friend/receiveList";
	}
	
//	친구목록 Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String friendList(@UserData ResMemberDetail detail, Model model) {
		List<FriendDTO> friendList = friendService.friendList(detail.getMember_email());
		int receive_count = friendService.receiveRequestCount(detail.getMember_email());	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		model.addAttribute("friendList", friendList);
		
		return "friend/friend_list";
	}
	
//	친구 닉네임 변경 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/nickname")
	public String friendName(FriendDTO friendDTO) {
		friendService.friendNickName(friendDTO);
		
		return "redirect:/friend/list";
	}
	
//	친구정보 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/info")
	public String friendInfo(FriendDTO friendDTO, @UserData ResMemberDetail detail, Model model,
							 @RequestParam(name = "friend_change", defaultValue = "none") String friend_change) {
		if (friend_change.equals("nick")) {
			friendService.friendNickName(friendDTO);
		}else if (friend_change.equals("memo")) {
			friendService.friendMemo(friendDTO);
		}
		
		
//		FriendDTO friendDTO = friendService.friendInfo(frndDTO);
//		friendDTO.setFriend_status(frndDTO.getFriend_status());		// 정방향 / 역방향 여부를 알려주는 변수
//		rttr.addFlashAttribute("friendDTO", friendDTO);
		
		int receive_count = friendService.receiveRequestCount(detail.getMember_email());	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		
//		return "redirect:/friend/info";
	    return String.format("redirect:/friend/info?friend_id=%d&friend_status=%s", friendDTO.getFriend_id(), friendDTO.getFriend_status());

	}

//	친구정보 Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/info")
	public String friendInfo(@RequestParam("friend_id") Long friend_id, @UserData ResMemberDetail detail,
							 @RequestParam("friend_status") String friend_status, Model model) {
		FriendDTO friendDTO = friendService.friendInfo(friend_id, friend_status);
		int receive_count = friendService.receiveRequestCount(detail.getMember_email());	// 받은 친구신청 수
		
		model.addAttribute("friendDTO", friendDTO);
		model.addAttribute("receive_count", receive_count);
		
		return "friend/friend_info";
	}
	
//	친구삭제 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/delete")
	public String friendDelete(@RequestParam("friend_id") Long friend_id,
							   @RequestParam("member_my_id") Long member_my_id,
							   @RequestParam("member_friend_id") Long member_friend_id) {
		// 친구테이블(member_my_id) == 친구요청테이블(member_receive_id)
		friendService.friendDelete(friend_id, member_my_id, member_friend_id);	// 친구, 친구요청 삭제 메서드
		
		return "redirect:/friend/list";
	}
	
}