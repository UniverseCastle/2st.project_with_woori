package com.planner.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.request.member.ReqMemberRestore;
import com.planner.dto.request.member.ReqMemberUpdate;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.FriendRole;
import com.planner.enums.Gender;
import com.planner.enums.MemberStatus;
import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.service.FriendService;
import com.planner.service.MemberService;
import com.planner.util.CommonUtils;
import com.planner.util.UserData;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	private final FriendService friendService;
	
//	<!-- =========================민형이 자료========================= -->
	/*소셜로그인에서 생긴 쿠키 제거 후 로그아웃*/
	@GetMapping("/anon/signout")
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		CommonUtils.removeCookiesAndSession(request, response);
		return "redirect:/member/logout";
	}


	//	회원가입 Get
	@GetMapping("/anon/insert")
	public String memberInsert() {
		return "/member/member_insert";
	}
	
	//	회원가입 Post
	@PostMapping("/anon/insert")
	public String memberInsert(MemberDTO memberDTO, RedirectAttributes rttr) {
		int result = memberService.memberInsert(memberDTO);
		rttr.addFlashAttribute("result", result);
		return "redirect:/planner/main";
	}

	//	로그인
	@GetMapping("/anon/login")
	public String memberLogin(@UserData ResMemberDetail detail,HttpServletRequest request,HttpServletResponse response) {
			if(detail != null &&detail.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				CommonUtils.removeCookiesAndSession(request, response);
				return"redirect:/member/anon/login";
			}
		return "/member/member_login";
	}

	/*로그인시에 회원탈퇴여부 검사*/
	@GetMapping("/anon")
	public String memberChk(@UserData ResMemberDetail detail, HttpServletRequest request,HttpServletResponse response) {
		if(detail.getMember_status().equals(MemberStatus.DELETE.getCode())) {
			CommonUtils.removeCookiesAndSession(request, response);
			throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);
		}
		return "redirect:/planner/main";
	}
	
	/*로그인 실패시 매핑*/
	@GetMapping("/anon/fail")
	public void loginFail() {
		throw new CustomException(ErrorCode.NO_ACCOUNT);
	}
	
	/*내 정보*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/auth/info")
	public String memberInfo(Model model,@UserData ResMemberDetail detail) {
		String gender = Gender.findNameByCode(detail.getMember_gender());
		model.addAttribute("detail", detail);
		model.addAttribute("gender", gender);
		return "/member/member_info"; 
	}
	
	/*비밀번호 확인 폼*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/auth/chk")
	public String passwordChkForm(@RequestParam(value = "url")String url, Model model) {
		model.addAttribute("url", url);
		return"/member/passwordChk";
	}
	
	/*비밀번호 확인*/
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/auth/chk")
	@ResponseBody
	public ResponseEntity<String> passwordChk(@RequestParam(value = "currentPw")String currentPw,@UserData ResMemberDetail member) {
		int result = memberService.passwordChk(currentPw,member);
		if(result ==1) {
			return ResponseEntity.ok("성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("실패");
	}
	
	/*회원정보 수정 폼*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/auth/update")
	public String memberUpdateForm(Model model,@UserData ResMemberDetail detail) {
		model.addAttribute("detail", detail);
		return "/member/member_update";
	}

	/*회원정보 수정*/
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/auth/update")
	public String memberUpdate(ReqMemberUpdate req) {
		memberService.memberUpdate(req);
		//TODO - 회원 정보 이메일 수정시에 이메일 인증 추가
		return "redirect:/member/auth/info";
	}
	
	/*회원 탈퇴*/
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/auth/delete")
	@ResponseBody
	public void memberDelete(@UserData ResMemberDetail detail,HttpServletRequest request,HttpServletResponse response) {
		CommonUtils.removeCookiesAndSession(request, response);
		memberService.memberDelete(detail.getMember_id());
	}
	
	/*회원복구 폼*/
	@GetMapping("/anon/restore")
	public String memberRestoreForm() {
		return "/member/member_restore";
	}
	
	/*회원 복구*/
	@PostMapping("/anon/restore")
	@ResponseBody
	public int memberRestore(ReqMemberRestore req) {
		int result = memberService.memberRestore(req);
		return result;
	}
	
//	<!-- =========================민형이 자료========================= -->
	
//	메인
//	@GetMapping("main")
//	private String main() {
//		return "member/member_main";
//	}
	
//	회원가입 Get
//	@GetMapping("insert")
//	public String memberInsert() {
//		
//		return "member/member_insert";
//	}
	
//	회원가입 Post
//	@PostMapping("insert")
//	public String memberInsert(MemberDTO memberDTO,
//							   RedirectAttributes rttr) {
//		rttr.addFlashAttribute("result", memberService.memberInsert(memberDTO));
//		
//		return "redirect:/member/main";
//	}
	
//	로그인
//	@GetMapping("login")
//	public String memberLogin() {
//		return "member/member_login";
//	}
	
//	로그아웃
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/logout")
	public String memberLogout() {
		return "member/member_main";
	}
	
//	회원찾기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/memberSearch")
	public String memberSearch(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
						   @RequestParam(name = "keyword", defaultValue = "!@#$%^&*()") String keyword,		// 키워드 기본값 특수문자로 초기 화면 없애기
						   Model model, Principal principal) {
//		TODO 페이징처리 유효성검사 하기
//		페이징 처리
		int pageSize = 10;
		int currentPage = pageNum;
		int start = (currentPage - 1) * pageSize + 1;
		int end = currentPage * pageSize;
		int count = 0;
		
		List<MemberDTO> list = memberService.memberSearch(principal, keyword, start, end);
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
		
		return "member/member_memberSearch";
	}
	
//	회원정보(내정보) Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/memberInfo")
	public String memberInfo(@ModelAttribute("member_email") String member_email, Principal principal,
							  Model model) {
		Long myId = memberService.findByMemberId(principal.getName());
		if (!member_email.isEmpty()) {		// 회원 정보인 경우
			MemberDTO memberDTO = memberService.memberInfo(member_email);
			model.addAttribute("memberDTO", memberDTO);
		}else {								// 내 정보인 경우
			MemberDTO memberDTO = memberService.memberInfo(principal.getName());
			model.addAttribute("memberDTO", memberDTO);
			model.addAttribute("myId", myId);	// 내 정보일 때 차별점을 주기 위해 시퀀스 넘김
		}
		int receive_count = friendService.receiveRequestCount(principal);	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		
		
		
		return "member/member_memberInfo";
	}
	
//	회원정보 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/memberInfo")
	public String memberInfo(@RequestParam("member_email") String member_email,
							 Principal principal, RedirectAttributes rttr) {
		rttr.addFlashAttribute("member_email", member_email);
		
		return "redirect:/member/memberInfo";
	}
}