package com.siwimi.webapi.repository;

import com.siwimi.webapi.domain.Favorite;

public interface FavoriteRepositoryCustom {
	Favorite queryFavorite(String creator, String targetObject, String objectType);
}
