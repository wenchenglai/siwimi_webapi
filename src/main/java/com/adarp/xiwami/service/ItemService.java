package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Item;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.FavoriteRepository;
import com.adarp.xiwami.repository.ItemRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRep;
	
	@Autowired
	FavoriteRepository favoriteRep;	
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
	public List<Item> findItems(String creatorId, 
								String requesterId,
								String status,String type,String condition,
								Double longitude,Double latitude,String qsDistance,
								String queryText) {					
		List<Item> itemList = itemRep.queryItem(creatorId,status,type,condition,longitude,latitude,qsDistance,queryText);
		
		// increment viewcount by 1, and save it to MongoDB
		for (int i=0; i<itemList.size(); i++) {
			Item item = itemList.get(i);
			// Populate isFavorite
			if (favoriteRep.queryFavorite(requesterId, item.getId(), "item") != null) {
				item.setIsFavorite(true);
			}
			itemList.set(i, item);
			// increment viewcount by 1, and save it to MongoDB
			item.setViewCount(item.getViewCount()+1);
			itemRep.saveItem(item);
		}
		
		return itemList;
	}
	
	public Item findByItemId(String id) {
		return itemRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Item addItem(Item newItem) {
		newItem.setIsDestroyed(false);
		newItem.setViewCount(0);
		newItem = updateLocation(newItem);
		itemRep.saveItem(newItem);		
		return newItem;
	}
	
	public Item updateItem(String id, Item updatedItem) {
		updatedItem.setId(id);
		updatedItem = updateLocation(updatedItem);
		return itemRep.saveItem(updatedItem);
	}
	
	public void deleteItem(String id) {
		Item item = itemRep.findOne(id);
		item.setIsDestroyed(true);
		itemRep.saveItem(item);
	}
	
	public Item updateLocation(Item item) {
		// lookup zipcode from the collection ZipCode;
		ZipCode thisZipCode = zipCodeRep.queryZipCode(item.getZipCode(), item.getCity(), item.getState());
		// set longitude and latitude 
		if (thisZipCode!=null) {
			double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
			item.setZipCode(thisZipCode.getZipCode());
			item.setLocation(location);
			item.setCity(thisZipCode.getTownship());
			item.setState(thisZipCode.getStateCode());
		}

		return item;
	}
}
