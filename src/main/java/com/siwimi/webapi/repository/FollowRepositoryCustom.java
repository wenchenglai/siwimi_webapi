package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Follow;

public interface FollowRepositoryCustom {
	List<Follow> queryFollow(String follower,String followee);
}
