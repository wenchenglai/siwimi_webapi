package com.adarp.xiwami.repository;

import com.adarp.xiwami.domain.Item;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ItemRepository {
	public List<Item> GetItems() throws Exception;
	
	public Item GetItemById(String id) throws Exception;
	
	public void AddItem(Item newItem) throws Exception;
	
	public void UpdateItem(String id, Item updateItem) throws Exception;
	
	public void DeleteItem(String id) throws Exception;		
}