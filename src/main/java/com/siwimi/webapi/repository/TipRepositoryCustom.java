package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Tip;

public interface TipRepositoryCustom {
	List<Tip> queryTip(String creatorId,
			           String status, String type, 
			           Double queryLongitude, Double queryLatitude, String qsDistance, 
			           String queryText,Integer page, Integer per_page);
	Tip saveTip(Tip newTip);
}
