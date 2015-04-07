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
		return followRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Follow addFollow(Follow newFollow) {
		newFollow.setIsDestroyed(false);
		return followRep.save(newFollow);
	}
	
	public Follow updateFollow (String id, Follow updatedFollow) {
		updatedFollow.setId(id);
		return followRep.save(updatedFollow);
	}
	
	public void deleteFollow (String id) {
		Follow follow = followRep.findOne(id);
		follow.setIsDestroyed(true);
		followRep.save(follow);
	}
}
