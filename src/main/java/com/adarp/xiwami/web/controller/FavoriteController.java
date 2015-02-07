package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Favorite;
import com.adarp.xiwami.service.FavoriteService;
import com.adarp.xiwami.web.dto.FavoriteSideload;

@RestController
public class FavoriteController {

	@Autowired
	private FavoriteService favoriteService;
	
	// Get Favorite by ID
	@RequestMapping(value = "/favorites/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Favorite> findByFavoriteId(@PathVariable("id") String id) {
		Map<String,Favorite> responseBody = new HashMap<String,Favorite>();			
		Favorite favorite = favoriteService.findByFavoriteId(id);
		responseBody.put("favorite", favorite);
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
	public void deleteFavorite(@PathVariable("id")String id) {
		favoriteService.deleteFavorite(id);
	}	
}
