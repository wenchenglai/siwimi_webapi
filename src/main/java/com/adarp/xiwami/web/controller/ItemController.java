package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.service.ItemService;
import com.adarp.xiwami.web.dto.ItemSideload;
import com.adarp.xiwami.domain.Item;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	// Get items by criteria
	@RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Item>> FindItems(
			@RequestParam(value="seller", required=false) String sellerId,
			@RequestParam(value="status", required=false) String status,
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="distance", required=false) String qsDistance, 
			@RequestParam(value="queryText", required=false) String queryText) {
		Map<String,List<Item>> responseBody = new HashMap<String,List<Item>>();
		List<Item> list = itemService.FindItems(sellerId,status,longitude,latitude,qsDistance,queryText);
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
	public Map<String,Item> AddItem(@RequestBody ItemSideload newItem){
		Item savedItem = itemService.AddItem(newItem.item);			
		Map<String,Item> responseBody = new HashMap<String,Item>();
		responseBody.put("item", savedItem);
		return responseBody;
	}	
	
	// Update Item
	@RequestMapping(value = "/items/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String,Item> UpdateItem(@PathVariable("id") String id, @RequestBody ItemSideload updatedItem) {
		Item savedItem = itemService.UpdateItem(id,updatedItem.item);
		
		Map<String,Item> responseBody = new HashMap<String,Item>();
		responseBody.put("item", savedItem);
		return responseBody;		
	}
	
	// Delete Item
	@RequestMapping (value = "/items/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteItem(@PathVariable("id")String id) {
		itemService.DeleteItem(id);
	}
}
