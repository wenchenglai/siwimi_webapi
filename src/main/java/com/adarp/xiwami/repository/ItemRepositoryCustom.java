package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Item;

public interface ItemRepositoryCustom {
		List<Item> queryItem(String sellerId,String status,Double longitude,Double latitude,String qsDistance,String queryText);
		Item saveItem(Item newItem);
}
