package com.adarp.xiwami.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Gossip;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.repository.GossipRepository;
import com.adarp.xiwami.repository.MemberRepository;

@Service
public class GossipService {

	@Autowired
	GossipRepository gossipRep;
	
	@Autowired
	private FamilyRepository familyRep;
	
	@Autowired
	private MemberRepository memberRep;
	
	public List<Gossip> FindGossips(String status, String userId, Double longitude,Double latitude) {
		if (status.equalsIgnoreCase("my")) {
			return gossipRep.findByUserAndIsDeletedIsFalse(userId);
		} else {
			List<Family> geoFamilies = familyRep.findByLocationNearAndIsDeletedIsFalse(new Point(longitude,latitude),new Distance(Double.valueOf(20.0),Metrics.MILES));
			
			// Retrieve the id of geoFamilies
			List<String> geoFamilyId = new ArrayList<String>();
			for (Family family : geoFamilies) {
				geoFamilyId.add(family.getId());
			}
			
			// Retrieve id of the geoMembers
			List<Member> geoMembers = memberRep.findByFamilyInAndIsDeletedIsFalse(geoFamilyId);
			List<String> geoMemberId = new ArrayList<String>();
			for (Member member : geoMembers) {
				geoMemberId.add(member.getId());
			}
			
			return gossipRep.findByUserInAndIsDeletedIsFalse(geoMemberId);
						
		}
	}
}
