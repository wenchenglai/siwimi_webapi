package com.adarp.xiwami.repository;

import com.adarp.xiwami.domain.ZipCode;

public interface ZipCodeRepositoryCustom {
			ZipCode queryZipCode(String zipCode, String city, String state);
}