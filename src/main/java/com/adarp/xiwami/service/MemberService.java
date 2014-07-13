package com.adarp.xiwami.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.repository.MemberRepository;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRep;
}
