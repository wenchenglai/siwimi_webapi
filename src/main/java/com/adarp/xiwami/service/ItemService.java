package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Item;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.ItemRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRep;
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
	public List<Item> findItems(String sellerId,String status,Double longitude,Double latitude,String qsDistance,String queryText) {					
		List<Item> itemList = itemRep.queryItem(sellerId,status, longitude,latitude,qsDistance,queryText);
		
		// increment viewcount by 1, and save it to MongoDB
		for (int i=0; i<itemList.size(); i++) {
			Item item = itemList.get(i);
			item.setViewCount(item.getViewCount()+1);
			itemRep.saveItem(item);
			itemList.set(i, item);
		}
		
		return itemList;
	}
	
	public Item findByItemId(String id) {
		return itemRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Item addItem(Item newItem) {
		newItem.setIsDestroyed(false);
		newItem.setViewCount(0);
		newItem = updateZipCode(newItem);
		itemRep.saveItem(newItem);		
		return newItem;
	}
	
	public Item updateItem(String id, Item updatedItem) {
		updatedItem.setId(id);
		updatedItem = updateZipCode(updatedItem);
		return itemRep.saveItem(updatedItem);
	}
	
	public void deleteItem(String id) {
		Item item = itemRep.findOne(id);
		item.setIsDestroyed(true);
		itemRep.saveItem(item);
	}
	
	public Item updateZipCode(Item item) {
		// lookup zipcode from the collection ZipCode;
		ZipCode thisZipCode = new ZipCode();
				
		// if the zipCode is not provided by the user
		if (item.getZipCode()==null) {				
			if (item.getCityState()==null){
				// if both zipcode and cityState are not completed, set default to 48105
				thisZipCode = zipCodeRep.findByzipCode(48105);
			} else {
				String [] parts = item.getCityState().split(",");
				String city = parts[0].trim();
				String stateCode = parts[1].trim();	
				thisZipCode = zipCodeRep.findByTownshipLikeIgnoreCaseAndStateCodeLikeIgnoreCase(city, stateCode);				
			}						
		} else {
			/** if the zipCode is provided by the user:
			   (1) ignore stateCity provided by the user, 
			   (2) lookup zipcode from the collection ZipCode
			   (3) please note that the type of zipcode is "int" in the mongoDB collection 
			**/
			thisZipCode = zipCodeRep.findByzipCode(Integer.parseInt(item.getZipCode()));			
		}
			
		// set longitude and latitude of the family object 
		double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
		item.setZipCode(thisZipCode.getZipCode());
		item.setLocation(location);
		item.setCityState(thisZipCode.getTownship()+", "+thisZipCode.getStateCode());	
		
		return item;
	}
}
