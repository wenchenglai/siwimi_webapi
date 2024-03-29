package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Follow;
import com.siwimi.webapi.repository.FollowRepository;

@Service
public class FollowService {

	@Autowired
	private FollowRepository followRep;
	
	public List<Follow> findFollows(String follower,String followee) {
		return followRep.queryFollow(follower, followee);
	}
	
	public Follow findByFollowId(String id) {
		return followRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Follow addFollow(Follow newFollow) {
		newFollow.setIsDeletedRecord(false);
		return followRep.save(newFollow);
	}
	
	public Follow updateFollow (String id, Follow updatedFollow) {
		updatedFollow.setId(id);
		return followRep.save(updatedFollow);
	}
	
	public Follow deleteFollow (String id) {
		Follow follow = followRep.findOne(id);
		if (follow == null)
			return null;
		else if (!follow.getIsDeletedRecord()) {
			follow.setIsDeletedRecord(true);
			return followRep.save(follow);				
		} else
			return null;		
	}
}
