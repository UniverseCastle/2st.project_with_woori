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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
//	(친추하기위한) 모든유저 정보
	@PreAuthorize("isAuthenticated()")
	@GetMapping("userInfo")
	public String userInfo(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
						   @RequestParam(name = "keyword", defaultValue = "!@#$%^&*()") String keyword,		// 키워드 기본값 특수문자로 초기 화면 없애기
						   Model model, Principal principal) {
//		TODO 페이징처리 유효성검사 하기
//		페이징 처리
		int pageSize = 10;
		int currentPage = pageNum;
		int start = (currentPage - 1) * pageSize + 1;
		int end = currentPage * pageSize;
		int count = 0;
		
		List<MemberDTO> list = memberService.memberList(principal, keyword, start, end);
		if (list.size() > 0) {
			memberService.findBySendId(principal, keyword);
			count = list.size();
		}
		
		int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
		int startPage = (int)((currentPage - 1) / 10) * 10 + 1;
		int pageBlock = 10;
		int endPage = startPage + pageBlock - 1;
		
		if (endPage >= pageCount) {
			endPage = pageCount;
		}
		
		model.addAttribute("count", count);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("pageNum", pageNum);
		
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("list", list);						// 친구신청 리스트 (친구신청 상태 담겨있음)
		model.addAttribute("friendRoles", FriendRole.values());	// FriendRole 상태 권한설정
		
		int receive_count = friendService.receiveRequestCount(principal);	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		
		return "member/member_userInfo";
	}
	
//	내 (회원)정보
	@PreAuthorize("isAuthenticated()")
	@GetMapping("myInfo")
	public String myInfo(Principal principal, Model model) {
		MemberDTO memberDTO = memberService.myInfo(principal.getName());
		model.addAttribute("memberDTO", memberDTO);
		
		return "member/member_myInfo";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("test")
	public String test () {
		
		return "member/test";
	}
}