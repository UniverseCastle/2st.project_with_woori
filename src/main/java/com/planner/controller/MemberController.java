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
import com.planner.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/*")
public class MemberController {
	
	private final MemberService memberService;
	
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
						   @RequestParam(name = "keyword", defaultValue = "@dk@)dK)@DJ@(") String keyword,
						   Model model, Principal principal) {
		int pageSize = 10;
		int currentPage = pageNum;
		int start = (currentPage - 1) * pageSize + 1;
		int end = currentPage * pageSize;
		int count = memberService.memberCount(keyword);
		
		List<MemberDTO> list = null;
		if (count > 0) {
			list = memberService.memberList(principal, keyword, start, end);
		}
		
		int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
		int startPage = (int)((currentPage - 1) / 10) * 10 + 1;
		int pageBlock = 10;
		int endPage = startPage + pageBlock - 1;
		
		if (endPage >= pageCount) {
			endPage = pageCount;
		}
		
//		List<MemberDTO> list = memberService.memberList(principal);
		
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("startPage", startPage);
//		model.addAttribute("pageBlock", pageBlock);
		model.addAttribute("endPage", endPage);
		
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("count", count);
		
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("list", list);						// 친구신청 리스트 (친구신청 상태 담겨있음)
		model.addAttribute("friendRoles", FriendRole.values());	// FriendRole 상태 권한설정
		
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