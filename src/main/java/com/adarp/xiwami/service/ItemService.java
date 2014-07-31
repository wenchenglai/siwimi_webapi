package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Item;
import com.adarp.xiwami.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRep;
	
	public List<Item> FindItems() {
		return itemRep.findAll();
	}
	
	public Item FindByItemId(String id) {
		return itemRep.findOne(id);
	}
	
	public Item AddItem(Item newItem) {
		newItem.setIsDeleted(false);
		itemRep.save(newItem);
		return newItem;
	}
	
	public Item UpdateItem(String id, Item updatedItem) {
		updatedItem.setId(id);
		return itemRep.save(updatedItem);
	}
	
	public void DeleteItem(String id) {
		Item item = itemRep.findOne(id);
		item.setIsDeleted(true);
		itemRep.save(item);
	}
}
