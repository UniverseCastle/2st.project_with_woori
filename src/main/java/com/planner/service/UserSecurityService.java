package com.planner.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
/*

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
	private final MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String member_email) throws UsernameNotFoundException {
		Optional<MemberDTO> _member = this.memberMapper.findByUser(member_email);
		if(_member.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		MemberDTO memberDTO = _member.get();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if ("admin".equals(member_email)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getName()));
		}else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getName()));
		}
		return new User(memberDTO.getMember_email(), memberDTO.getMember_password(), authorities);
	}
}
 */