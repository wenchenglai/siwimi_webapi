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
								String status,
								Double longitude,Double latitude,String qsDistance,
								String queryText) {					
		List<Item> itemList = itemRep.queryItem(creatorId, status, longitude,latitude,qsDistance,queryText);
		
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
		ZipCode thisZipCode = new ZipCode();
				
		// if the zipCode is not provided by the user
		if (item.getZipCode() == null) {				
			// Front-end must provide City and State
			String city = item.getCity();
			String state = item.getState();
			if ((city != null) && (state != null)) {
				thisZipCode = zipCodeRep.findByTownshipLikeIgnoreCaseAndStateLikeIgnoreCase(city, state);		
			}								
		} else {
			/** if the zipCode is provided by the the front-end:
			   (1) ignore state/City provided by the front-end, 
			   (2) lookup zipcode from the collection ZipCode
			   (3) The type of zipcode is "int" in the mongoDB collection 
			**/
			thisZipCode = zipCodeRep.findByzipCode(Integer.parseInt(item.getZipCode()));			
		}
			
		// set longitude and latitude of the family object 
		double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
		item.setZipCode(thisZipCode.getZipCode());
		item.setLocation(location);
		item.setCity(thisZipCode.getTownship());
		item.setState(thisZipCode.getStateCode());	
		
		return item;
	}
}
