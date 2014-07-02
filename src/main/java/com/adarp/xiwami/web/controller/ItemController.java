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

import com.adarp.xiwami.repository.*;
import com.adarp.xiwami.domain.Item;
import com.adarp.xiwami.web.dto.*;

@RestController
public class ItemController {

	@Autowired
	private ItemRepository itemRep;

	// Get all items
	@RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Item>> FindItems() {
		try {				
			Map<String,List<Item>> responseBody = new HashMap<String,List<Item>>();
			//List<Item> list = itemRep.GetItems();
			List<Item> list = itemRep.findAll();
			responseBody.put("item", list);
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Item.");
			return null;
		}
	}

	// Get item by ID
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Item> FindByItemId(@PathVariable("id") String id) {
		
		try {
			Map<String,Item> responseBody = new HashMap<String,Item>();			
			//Item item = itemRep.GetItemById(id);
			Item item = itemRep.findOne(id);
			responseBody.put("item", item);
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Item.");
			return null;
		}
	}
	
	// Add New Item
	@RequestMapping(value = "/items", method = RequestMethod.POST, produces = "application/json")
	public void AddItem(@RequestBody ItemSideload newItem)
	{
		try {
			//itemRep.AddItem(newItem.item);
			itemRep.save(newItem.item);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to add Item.");
		}		
	}	
	
	// Update Item
	@RequestMapping(value = "/items/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void EditItem(@PathVariable("id") String id, @RequestBody ItemSideload updatedItem)
	{
		try {
			itemRep.UpdateItem(id,updatedItem.item);			
			//updatedItem.item.set_Id(id);
			//itemRep.save(updatedItem.item);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to update Item.");
		}
	}
	
	// Delete Item
	@RequestMapping (value = "/items/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteItem(@PathVariable("id")String id) {
		try {
			itemRep.DeleteItem(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to delete Item.");			
		}
	}	
}
