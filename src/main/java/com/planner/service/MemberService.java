package com.planner.service;

import java.security.Principal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.request.member.ReqMemberRestore;
import com.planner.dto.request.member.ReqMemberUpdate;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberRole;
import com.planner.enums.MemberStatus;
import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.mapper.FriendMapper;
import com.planner.mapper.MemberMapper;
import com.planner.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final FriendMapper friendMapper;
	private final PasswordEncoder passwordEncoder;
	
//	<!-- =========================민형이 자료========================= -->
	// 회원가입
		@Transactional
		public int memberInsert(MemberDTO memberDTO) {
			if (isMember(memberDTO.getMember_email())) {
				throw new CustomException(ErrorCode.ID_DUPLICATE);// 이메일(아이디 중복)에 대한 커스텀예외
			}
			memberDTO.setMember_role(MemberRole.USER.getType());
			memberDTO.setMember_password(passwordEncoder.encode(memberDTO.getMember_password()));
			return memberMapper.memberInsert(memberDTO);
		}

		/* 내 정보 */
		@Transactional(readOnly = true)
		public ResMemberDetail memberDetail(String member_email) {
			ResMemberDetail detail = memberMapper.findByEmail(member_email);
			return detail;
		}

		/* 회원 정보 수정 */
		@Transactional
		public void memberUpdate(ReqMemberUpdate req) {
			memberMapper.memberUpdate(req);
		}

		/* 비번체크 */
		@Transactional(readOnly = true)
		public int passwordChk(String currnetPw, ResMemberDetail member) {
			int result = 0;
			if (member != null && !member.getOauth_id().equals("none")) {
				return result = 1;
			}
			if (member != null && passwordEncoder.matches(currnetPw, member.getMember_password())) {
				return result = 1;
			}
			return result;
		}

		/* 회원 탈퇴 */
		@Transactional
		public void memberDelete(Long member_id) {
			memberMapper.changeMemberStatus(member_id, MemberStatus.DELETE.getCode());
		}

		/* 회원 복구 */
		@Transactional
		public int memberRestore(ReqMemberRestore req) {
			int result = 0;
			if (!CommonUtils.isEmpty(req.getOauth_type())) { // 소셜 로그인일때
				ResMemberDetail memberDetail = memberMapper.findByEmailAndOAuthType(req.getCurrentEmail(),
						req.getOauth_type());
				result = changeMemberStatus(memberDetail);
				return result;
			}
			if (CommonUtils.isEmpty(req.getOauth_type())) {	// 일반로그인일때
				ResMemberDetail memberDetail = memberMapper.findByEmail(req.getCurrentEmail());
				int pwChk = passwordChk(req.getCurrentPassword(), memberDetail);

				if (pwChk == 1) {
					result = changeMemberStatus(memberDetail);
					return result;
				}
			}
			return result;
		}

		/* 회원 상태변경 */
		@Transactional
		private int changeMemberStatus(ResMemberDetail memberDetail) {
			int result = 0;
			if (!CommonUtils.isEmpty(memberDetail)) {
				result = memberMapper.changeMemberStatus(memberDetail.getMember_id(), MemberStatus.BASIC.getCode());
				return result;
			}
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}

		/* 회원체크 */
		@Transactional
		public boolean isMember(String email) {
			boolean result = true;
			ResMemberDetail user = memberMapper.findByEmail(email);
			if (user == null) {
				result = false;
			}
			// 탈퇴한 회원이면 예외 발생
			if (user != null && user.getMember_status().equals(MemberStatus.DELETE.getCode())) {
				throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);
			}
			return result;

		}
	
//	<!-- =========================민형이 자료========================= -->
	
//	회원 이메일로 객체 가져오기
//	public MemberDTO findByMember(String member_email) {
//		return memberMapper.findByMember(member_email);
//	}
//	public Optional<MemberDTO> findByUser(String member_email){
//		return memberMapper.findByUser(member_email);
//	}
	
//	회원 이메일로 시퀀스 찾기
	public Long findByMemberId(String member_email) {
		return memberMapper.findByMemberId(member_email);
	}
	
//	회원가입
//	public int memberInsert(MemberDTO memberDTO) {
//		memberDTO.setMember_password(passwordEncoder.encode(memberDTO.getMember_password()));
//		
//		return memberMapper.memberInsert(memberDTO);
//	}
	
//	로그인
//	public int memberLogin(String member_email, String member_password) {
//		return memberMapper.memberLogin(member_email, member_password);
//	}
	
//	전체 회원 수
//	public int memberCount(String keyword) {
//		return memberMapper.memberCount(keyword);
//	}
	
//	회원 검색
	public List<MemberDTO> memberSearch(String member_email, String keyword, int start, int end){
//		int count = 0;
		Long myId = memberMapper.findByMemberId(member_email);
		List<MemberDTO> list = memberMapper.memberSearch(myId, keyword, start, end);
		List<MemberDTO> sendIdList = memberMapper.findBySendId(myId, keyword);
		
		if (!sendIdList.isEmpty()) {
			list.removeAll(sendIdList);			// 보낸사람 기준 여러명에게 보낸 만큼 중복되어 나오는 데이터 삭제
		}
		for (MemberDTO memberDTO : list) {		// 리스트에서 신청상태를 표시하기 위해 set
			String status = friendMapper.friendRequestStatus(memberDTO.getMember_id(), myId);
			memberDTO.setFriend_request_status(status);
//			memberDTO.setMember_count(count);	// 중복되어 나오는 데이터를 삭제하고 난 후 회원 수를 리턴
		}
		return list;
	}
	
//	친구신청 보낸 아이디 찾기
	public List<MemberDTO> findBySendId(String member_email, @Param("keyword") String keyword) {
		Long member_id = memberMapper.findByMemberId(member_email);
		return memberMapper.findBySendId(member_id, keyword);
	}
	
//	회원정보
	public MemberDTO memberInfo(String member_email) {
		return memberMapper.memberInfo(member_email);
	}
	
}