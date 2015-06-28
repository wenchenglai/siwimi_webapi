package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Item;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.ItemService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.ItemSideload;
import com.siwimi.webapi.web.dto.ItemSideloadList;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private MemberService memberService;
	
	// Get items by criteria
	@RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
	public ItemSideloadList findItems(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="requester", required=false) String requesterId, // userId who is sending this query request				
			@RequestParam(value="status", required=false) String status,
			@RequestParam(value="type", required=false) String type,
			@RequestParam(value="condition", required=false) String condition,
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="distance", required=false) String qsDistance, 
			@RequestParam(value="queryText", required=false) String queryText,
			@RequestParam(value="pageNumber", required=false) Integer pageNumber, 
			@RequestParam(value="pageSize", required=false) Integer pageSize,
			@RequestParam(value="sortBy", required=false) String sortBy) {
		
		ItemSideloadList responseBody = new ItemSideloadList();
		List <Item> itemList = itemService.findItems(creatorId,requesterId,status,type,condition,
													 longitude,latitude,qsDistance,queryText,
                                                     pageNumber,pageSize,sortBy);
		Set<Member> members = new HashSet<Member>();
		if (itemList!=null) {
			for (Item item : itemList) {
				Member member = memberService.findByMemberId(item.getCreator(),null);
				// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
				if (member!=null)
					members.add(member);
			}
		} else {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			itemList = new ArrayList<Item>();
		}
		responseBody.items = itemList;
		responseBody.members = new ArrayList<Member>(members);
		return responseBody;
	}

	// Get item by ID
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Item> findByItemId(@PathVariable("id") String id) {		
		Map<String,Item> responseBody = new HashMap<String,Item>();			
		Item item = itemService.findByItemId(id);
		responseBody.put("item", item);
		return responseBody;
	}
	
	// Add New Item
	@RequestMapping(value = "/items", method = RequestMethod.POST, produces = "application/json")
	public Map<String,Item> addItem(@RequestBody ItemSideload newItem){
		Item savedItem = itemService.addItem(newItem.item);			
		Map<String,Item> responseBody = new HashMap<String,Item>();
		responseBody.put("item", savedItem);
		return responseBody;
	}	
	
	// Update Item
	@RequestMapping(value = "/items/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String,Item> updateItem(@PathVariable("id") String id, @RequestBody ItemSideload updatedItem) {
		Item savedItem = itemService.updateItem(id,updatedItem.item);
		
		Map<String,Item> responseBody = new HashMap<String,Item>();
		responseBody.put("item", savedItem);
		return responseBody;		
	}
	
	// Delete Item
	@RequestMapping (value = "/items/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteItem(@PathVariable("id")String id) {
		itemService.deleteItem(id);
	}
}
