package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Item;
import com.siwimi.webapi.domain.Location;
import com.siwimi.webapi.repository.FavoriteRepository;
import com.siwimi.webapi.repository.ItemRepository;
import com.siwimi.webapi.repository.LocationRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRep;
	
	@Autowired
	private FavoriteRepository favoriteRep;	
	
	@Autowired
	private LocationRepository locationRep;
	
	public List<Item> findItems(String creatorId, 
								String requesterId,
								String status,String type,String condition,
								Double longitude,Double latitude,String qsDistance,
								String queryText,
								Integer page,
								Integer per_page) {					
		List<Item> itemList = itemRep.queryItem(creatorId,status,type,condition,
											    longitude,latitude,qsDistance,queryText,
                                                page,per_page);
		
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
		return itemRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Item addItem(Item newItem) {
		newItem.setIsDeletedRecord(false);
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
		item.setIsDeletedRecord(true);
		itemRep.saveItem(item);
	}
	
	public Item updateLocation(Item item) {
		// lookup location from the collection Location;
		Location thisLocation = locationRep.queryLocation(item.getZipCode(), item.getCity(), item.getState());
		// set longitude and latitude 
		if (thisLocation!=null) {
			double[] location = {thisLocation.getLongitude(), thisLocation.getLatitude()};
			item.setZipCode(thisLocation.getZipCode());
			item.setLocation(location);
			item.setCity(thisLocation.getTownship());
			item.setState(thisLocation.getStateCode());
		}

		return item;
	}
}
