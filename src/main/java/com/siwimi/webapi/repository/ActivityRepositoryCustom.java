package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Activity;

public interface ActivityRepositoryCustom {
	List<Activity> queryActivity(String creatorId,String status,String type,Integer period,String fromTime, String toTime,
			                     Double queryLongitude,Double queryLatitude,String qsDistance,String ageGroup,String stage,
			                     boolean isFree,String queryText,
			                     Integer page, Integer per_page,String sortBy);
	Boolean isExisted(String customData, String parser);
	Activity saveActivity(Activity newActivity);
}
