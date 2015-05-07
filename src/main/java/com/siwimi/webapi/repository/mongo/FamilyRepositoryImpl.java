package com.siwimi.webapi.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Family;
import com.siwimi.webapi.repository.FamilyRepositoryCustom;

@Repository
public class FamilyRepositoryImpl implements FamilyRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Family saveFamily(Family newFamily){
		mongoTemplate.indexOps(Family.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newFamily,"Family");
		return newFamily;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public List<String> findGeoFamiliesId(Double longitude,Double latitude,String qsDistance) {
		
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDeletedRecord").is(false));
		
		if ((longitude != null) && (latitude != null) && (qsDistance!= null)) {			
			double distance = 0.0;
			String [] parts = qsDistance.split(" ");
			if (parts[1].toLowerCase().contains("mile"))
				distance = Double.parseDouble(parts[0])/3959;
			else	
				distance = Double.parseDouble(parts[0])/6371;
					
			criterias.add(new Criteria().where("location").nearSphere(new Point(longitude,latitude)).maxDistance(distance));			
		}

		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		List<Family> geoFamilies = mongoTemplate.find(new Query(c),Family.class,"Family");
		
		List<String> geoFamilyId = new ArrayList<String>();
		for (Family family : geoFamilies) {
			geoFamilyId.add(family.getId());
		}
		
		return geoFamilyId;
	}
	

}
