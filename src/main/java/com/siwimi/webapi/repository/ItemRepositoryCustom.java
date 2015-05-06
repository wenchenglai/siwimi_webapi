package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Item;

public interface ItemRepositoryCustom {
		List<Item> queryItem(String sellerId,String status,String type,String condition,
				             Double longitude,Double latitude,String qsDistance,String queryText,
		                     Integer page, Integer per_page);
		Item saveItem(Item newItem);
}
