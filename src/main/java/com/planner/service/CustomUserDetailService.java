package com.planner.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.MemberDTO;
import com.planner.mapper.MemberMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String member_email) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        //이메일 체크
        Optional<MemberDTO> omember = this.memberMapper.findByUser(member_email);

        if(omember.isPresent()) {
            MemberDTO memberDTO = omember.get();
            grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            return new User(memberDTO.getMember_email(), memberDTO.getMember_password(), grantedAuthorities);
        } else {
            // DB에 정보가 존재하지 않으므로 exception 호출
            throw new UsernameNotFoundException("user doesn't exist, email : " + member_email);
        }
    }
}