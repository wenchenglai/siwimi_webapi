package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Favorite;
import com.siwimi.webapi.repository.FavoriteRepository;

@Service
public class FavoriteService {
	
	@Autowired
	private FavoriteRepository favoriteRep;	
	
	public List<Favorite> query(String creator, String targetObject, String objectType) {
		return favoriteRep.queryFavorite(creator, targetObject, objectType);
	}
	
	public Favorite findByFavoriteId (String id) {
		return favoriteRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Favorite addFavorite(Favorite newFavorite) {
		// Trying to search if user gave the same Favorite before.
		List<Favorite> existedFavorites = favoriteRep.queryFavorite(newFavorite.getCreator(), 
				                                                    newFavorite.getTargetObject(), newFavorite.getObjectType());
		if ((existedFavorites == null) || (existedFavorites.isEmpty())) {
			// User never give this favorite before : save new Favorite
			newFavorite.setIsDeletedRecord(false);
			return favoriteRep.save(newFavorite);
		} else
			return null;
	}
	
	public Favorite updateFavorite(String id, Favorite updatedFavorite) {
		updatedFavorite.setId(id);
		return favoriteRep.save(updatedFavorite);
	}
	
	public void deleteFavorite(String id) {
		Favorite Favorite = favoriteRep.findOne(id);
		Favorite.setIsDeletedRecord(true);
		favoriteRep.save(Favorite);
	}	
}
