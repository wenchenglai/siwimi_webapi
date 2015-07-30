package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Feed;

public interface FeedRepositoryCustom {
		List<Feed> query(String requesterId);
}
