package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Activity;

public interface ActivityRepositoryCustom {
	List<Activity> queryActivity(String creatorId,String status,Double queryLongitude,Double queryLatitude,String qsDistance,String queryText);
	Activity saveActivity(Activity newActivity);
}
