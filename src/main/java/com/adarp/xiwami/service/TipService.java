package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Tip;
import com.adarp.xiwami.repository.TipRepository;

@Service
public class TipService {

	@Autowired
	TipRepository tipRep;
	
	public List<Tip> FindByType(String type) {

			return tipRep.findByTypeAndIsDeletedIsFalse(type);
			/*
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
			return tipRep.findByUserInAndIsDeletedIsFalse(geoMemberId);
			*/			

	}
	
	public Tip FindByTipId(String id) {
		return tipRep.findOne(id);
	}
	
	public Tip AddTip(Tip newTip) {
		newTip.setIsDeleted(false);
		return tipRep.save(newTip);
	}
	
	public Tip UpdateTip(String id, Tip updatedTip) {
		updatedTip.setId(id);
		return tipRep.save(updatedTip);
	}
	
	public void DeleteTip(String id) {
		Tip tip = tipRep.findOne(id);
		tip.setIsDeleted(true);
		tipRep.save(tip);
	}
}
