package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Follow;

public interface FollowRepositoryCustom {
	List<Follow> queryFollow(String follower,String followee);
}
