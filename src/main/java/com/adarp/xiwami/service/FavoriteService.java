package com.adarp.xiwami.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Favorite;
import com.adarp.xiwami.repository.FavoriteRepository;

@Service
public class FavoriteService {
	
	@Autowired
	private FavoriteRepository favoriteRep;	
	
	public Favorite findByFavoriteId (String id) {
		return favoriteRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Favorite addFavorite(Favorite newFavorite) {
		// Trying to search if user gave the same Favorite before.
		Favorite existedFavorite = favoriteRep.queryFavorite(newFavorite.getCreator(), newFavorite.getTargetObject(), newFavorite.getObjectType());
		// User never give favorite before : save new Favorite
		if (existedFavorite == null) {
			newFavorite.setIsDestroyed(false);
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
		Favorite.setIsDestroyed(true);
		favoriteRep.save(Favorite);
	}	
}
