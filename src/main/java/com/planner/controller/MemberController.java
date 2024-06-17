package com.planner.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planner.dto.FriendRequestDTO;
import com.planner.dto.MemberDTO;
import com.planner.enums.FriendRole;
import com.planner.service.FriendService;
import com.planner.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/*")
public class MemberController {
	
	private final MemberService memberService;
	private final FriendService friendService;
	
//	메인
	@GetMapping("main")
	private String main() {
		return "member/member_main";
	}
	
//	회원가입 Get
	@GetMapping("insert")
	public String memberInsert() {
		
		return "member/member_insert";
	}
	
//	회원가입 Post
	@PostMapping("insert")
	public String memberInsert(MemberDTO memberDTO,
							   RedirectAttributes rttr) {
		rttr.addFlashAttribute("result", memberService.memberInsert(memberDTO));
		
		return "redirect:/member/main";
	}
	
//	로그인
	@GetMapping("login")
	public String memberLogin() {
		return "member/member_login";
	}
	
//	로그아웃
	@PreAuthorize("isAuthenticated()")
	@GetMapping("logout")
	public String memberLogout() {
		return "member/member_main";
	}
	
//	모든유저 정보
	@PreAuthorize("isAuthenticated()")
	@GetMapping("userInfo")
	public String userInfo(Model model, Principal principal) {
		List<MemberDTO> list = memberService.memberList(principal);
		model.addAttribute("list", list);						// 친구신청 리스트 (친구신청 상태 담겨있음)
		model.addAttribute("friendRoles", FriendRole.values());	// FriendRole 상태 권한설정
//		Long myId = memberService.findByMemberId(principal.getName());
//		List<FriendRequestDTO> sendIdList = memberService.findBySendIdList(principal);	// 나의(신청 받은) 아이디로 보낸아이디(들) 검색한 리스트
//			System.out.println("컨트롤러 센드리스트"+sendIdList);
		
//		model.addAttribute("sendIdList", sendIdList);
		
//		for (FriendRequestDTO sendId : sendIdList) {
//			System.out.println(sendId.getFriend_request_status());
//		}
		
		List<FriendRequestDTO> receiveList = friendService.receiveRequestList(principal);
		
		model.addAttribute("receiveList", receiveList);
		
		return "member/member_userInfo";
	}
	
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("userInfo")
//	public String userInfo(Principal principal, Model model) {
//		List<MemberDTO> list = memberService.memberList(principal);
//		
//		model.addAttribute("list", list);
//		
//		return "member/member_userInfo";
//	}

}