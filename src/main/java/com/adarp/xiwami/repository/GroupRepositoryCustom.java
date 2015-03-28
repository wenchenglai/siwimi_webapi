package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Group;

public interface GroupRepositoryCustom {
	List<Group> queryGroup(String creatorId,String queryText);

}
