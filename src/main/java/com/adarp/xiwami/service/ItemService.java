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
	
	public void AddItem(Item newItem) {
		newItem.setIsDeleted(false);
		itemRep.save(newItem);
	}
	
	public void EditItem(String id, Item updatedItem) {
		updatedItem.set_Id(id);
		itemRep.save(updatedItem);
	}
	
	public void DeleteItem(String id) {
		Item item = itemRep.findOne(id);
		item.setIsDeleted(true);
		itemRep.save(item);
	}
}
