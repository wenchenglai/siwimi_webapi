package com.adarp.xiwami.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.MemberRepository;

@Service
public class AuthService {
	
	@Autowired
	private MemberRepository memberRep;
	
	public Member getMember(Member member) {
		return memberRep.findByEmailIgnoreCaseAndPasswordAndIsDestroyedIsFalse(member.getEmail(), member.getPassword());		
	}


}
