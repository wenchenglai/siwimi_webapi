package com.adarp.xiwami.repository;

import com.adarp.xiwami.domain.Favorite;

public interface FavoriteRepositoryCustom {
	Favorite queryFavorite(String creator, String targetObject, String objectType);
}
