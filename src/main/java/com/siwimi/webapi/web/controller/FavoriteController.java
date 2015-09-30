package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Favorite;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.FavoriteService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.FavoriteSideload;
import com.siwimi.webapi.web.dto.FavoriteSideloadList;

@RestController
public class FavoriteController {

	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private MemberService memberService;
	
	// Get Favorite by ID
	@RequestMapping(value = "/favorites/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Favorite> findByFavoriteId(@PathVariable("id") String id) {
		Map<String,Favorite> responseBody = new HashMap<String,Favorite>();			
		Favorite favorite = favoriteService.findByFavoriteId(id);
		responseBody.put("favorite", favorite);
		return responseBody;
	}
	
	// Query favorites
	@RequestMapping(value = "/favorites", method = RequestMethod.GET, produces = "application/json")
	public FavoriteSideloadList find(@RequestParam(value="creator", required=true) String creatorId,
								 @RequestParam(value="targetObject", required=false) String targetObject,
								 @RequestParam(value="objectType", required=false) String objectType) {
		FavoriteSideloadList responseBody = new FavoriteSideloadList();			
		List<Favorite> favorites = favoriteService.query(creatorId, targetObject, objectType);
		Set<Member> members = new HashSet<Member>();
		if (favorites != null) {
			for (Favorite favorite :  favorites) {
				Member member = memberService.findByMemberId(favorite.getCreator());
				// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
				if (member!=null)
					members.add(member);				
			}
		} else 
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			favorites = new ArrayList<Favorite>();

		responseBody.favorites = favorites;
		responseBody.members = new ArrayList<Member>(members);
		return responseBody;
	}
	
	// Add New Favorite
	@RequestMapping(value = "/favorites", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Favorite> addFavorite(@RequestBody FavoriteSideload newFavorite) {
		Favorite savedFavorite = favoriteService.addFavorite(newFavorite.favorite);		
		Map<String, Favorite> responseBody = new HashMap<String, Favorite>();
		responseBody.put("favorite", savedFavorite);
		
		return responseBody;		
	}	
	
	// Update Favorite
	@RequestMapping(value = "/favorites/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Favorite> updateFavorite(@PathVariable("id") String id, @RequestBody FavoriteSideload updatedFavorite) {
		Favorite savedFavorite = favoriteService.updateFavorite(id, updatedFavorite.favorite);
		Map<String, Favorite> responseBody = new HashMap<String, Favorite>();
		responseBody.put("favorite", savedFavorite);
		
		return responseBody;			
	}
	
	// Delete Favorite
	@RequestMapping (value = "/favorites/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFavorite(@PathVariable("id")String id) {
		favoriteService.deleteFavorite(id);
	}	
}
