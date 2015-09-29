package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Favorite;

public interface FavoriteRepositoryCustom {
	List<Favorite> queryFavorite(String creator, String targetObject, String objectType);
}
