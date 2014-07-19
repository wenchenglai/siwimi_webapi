package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.ZipCode;

public interface ZipCodeRepository extends MongoRepository<ZipCode, String>, ZipCodeRepositoryCustom{
	public ZipCode findByzipCode(int zipCode);
	public ZipCode findByTownshipAndStateCode(String township, String stateCode);

}
