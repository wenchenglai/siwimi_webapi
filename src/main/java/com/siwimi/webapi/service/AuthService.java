package com.siwimi.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.repository.MemberRepository;

@Service
public class AuthService {
	
	@Autowired
	private MemberRepository memberRep;
	
	public Member getMember(Member member) {
		return memberRep.LoginExistingMember(member.getEmail(), member.getPassword());		
	}


}
