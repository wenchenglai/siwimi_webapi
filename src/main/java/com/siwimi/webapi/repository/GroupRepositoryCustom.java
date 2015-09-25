package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Group;

public interface GroupRepositoryCustom {
	List<Group> queryGroup(String creatorId, String memberId, String queryText);

}
