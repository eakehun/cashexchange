package com.hourfun.cashexchange.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.Member;
import com.hourfun.cashexchange.repository.MemberRepository;

@Service
public class CustomSecurityService implements UserDetailsService{
	
	@Autowired
	private MemberRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Member member = repository.findById(username);
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		
		authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getAuth()));
		
		User user = new User(member.getId(), member.getPwd(), authorities);
		
		return user;
	}

}
