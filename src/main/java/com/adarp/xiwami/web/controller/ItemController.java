package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.service.ItemService;
import com.adarp.xiwami.web.dto.ItemSideload;
import com.adarp.xiwami.domain.Item;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;

	// Get all items
	@RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Item>> FindItems() {
		Map<String,List<Item>> responseBody = new HashMap<String,List<Item>>();
		List<Item> list = itemService.FindItems();
		responseBody.put("item", list);
		return responseBody;
	}

	// Get item by ID
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Item> FindByItemId(@PathVariable("id") String id) {		
		Map<String,Item> responseBody = new HashMap<String,Item>();			
		Item item = itemService.FindByItemId(id);
		responseBody.put("item", item);
		return responseBody;
	}
	
	// Add New Item
	@RequestMapping(value = "/items", method = RequestMethod.POST, produces = "application/json")
	public void AddItem(@RequestBody ItemSideload newItem){
		itemService.AddItem(newItem.item);	
	}	
	
	// Update Item
	@RequestMapping(value = "/items/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void EditItem(@PathVariable("id") String id, @RequestBody ItemSideload updatedItem) {
		itemService.EditItem(id,updatedItem.item);
	}
	
	// Delete Item
	@RequestMapping (value = "/items/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteItem(@PathVariable("id")String id) {
		itemService.DeleteItem(id);
	}
}
