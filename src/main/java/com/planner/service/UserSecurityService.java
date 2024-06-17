package com.planner.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.planner.dto.MemberDTO;
import com.planner.enums.UserRole;
import com.planner.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
	
	private final MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String member_userid) throws UsernameNotFoundException {
		Optional<MemberDTO> _member = this.memberMapper.findByUser(member_userid);
		if(_member.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		MemberDTO memberDTO = _member.get();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if ("admin".equals(member_userid)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getName()));
		}else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getName()));
		}
		return new User(memberDTO.getMember_userid(), memberDTO.getMember_password(), authorities);
	}
}