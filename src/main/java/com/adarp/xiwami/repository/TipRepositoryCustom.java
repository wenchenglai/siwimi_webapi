package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Tip;

public interface TipRepositoryCustom {
	List<Tip> queryTip(String type,Double queryLongitude,Double queryLatitude,String qsDistance,String queryText);
	Tip saveTip(Tip newTip);
}
