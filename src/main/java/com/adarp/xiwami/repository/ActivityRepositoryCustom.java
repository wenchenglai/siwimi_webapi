package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Activity;

public interface ActivityRepositoryCustom {
	List<Activity> queryActivity(String creatorId,String status,String type,Integer period,String fromTime, String toTime,
			                     Double queryLongitude,Double queryLatitude,String qsDistance,String queryText);
	Activity saveActivity(Activity newActivity);
}
