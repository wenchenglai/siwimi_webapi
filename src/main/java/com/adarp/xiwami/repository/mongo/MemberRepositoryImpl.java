package com.adarp.xiwami.repository.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.MemberRepositoryCustom;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Member> query(String familyId, String queryText) {
		
		// If both input parameters are null, no need to query the existing member
		if ((familyId == null) && (queryText == null)) {
			return null;
		} else {		
			List<Criteria> criterias = new ArrayList<Criteria>();

			criterias.add(new Criteria().where("isDestroyed").is(false));
			
			// Search by family ID
			if (familyId != null) {
				criterias.add(new Criteria().where("family").is(familyId));
			}
			
			// Search name by queryText
			if (queryText != null) {
				criterias.add(new Criteria().orOperator(Criteria.where("firstName").regex(queryText.trim(), "i"),
	                    								Criteria.where("lastName").regex(queryText.trim(), "i")));
			}
			
			Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
			
			//Sort by "firstName" --> then sort by "lastName"
			Query q = new Query(c).with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"firstName").and(new Sort(Sort.DEFAULT_DIRECTION.ASC,"lastName")));
			
			return mongoTemplate.find(q, Member.class, "Member");
		}			
	}

	// Search member id from DB id or facebook id.
	@SuppressWarnings("static-access")
	@Override
	public Member findByid(String id) {
		List<Criteria> criterias = new ArrayList<Criteria>();

		criterias.add(new Criteria().where("isDestroyed").is(false));
		criterias.add(new Criteria().orOperator(Criteria.where("id").is(id),
				                                Criteria.where("facebookId").is(id)));
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));		
		return mongoTemplate.findOne(new Query(c), Member.class, "Member");
	}
	
	
	@SuppressWarnings("static-access")
	@Override
	public Member findExistingMember(String facebookId, String email) {
		
		// If both input parameters are null, no need to query the existing member
		if (facebookId == null && email == null) {
			return null;
		} else {
			List<Criteria> criterias = new ArrayList<Criteria>();

			criterias.add(new Criteria().where("isDestroyed").is(false));
		
			if (facebookId != null) {
				criterias.add(new Criteria().where("facebookId").is(facebookId));
			}
		
			if (email != null) {
				criterias.add(new Criteria().where("email").regex(email.trim(), "i"));
			}
		
			Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		
			return mongoTemplate.findOne(new Query(c), Member.class, "Member");
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public Member LoginExistingMember(String email, String password) {
		
		// If either input parameters is null, no need to query member
		if ((email == null) || (password == null)) {
			return null;
		} else {
			List<Criteria> criterias = new ArrayList<Criteria>();

			criterias.add(new Criteria().where("isDestroyed").is(false));
			criterias.add(new Criteria().where("email").is(email));
			criterias.add(new Criteria().where("password").is(password));
		
			Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		
			return mongoTemplate.findOne(new Query(c), Member.class, "Member");
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public Set<String> findFamilies(List<String> geoFamiliesId,Integer fromAge,Integer toAge,String[] languages) {
		
		List<Criteria> criterias = new ArrayList<Criteria>();

		criterias.add(new Criteria().where("isDestroyed").is(false));
		
		if (geoFamiliesId != null) {
			criterias.add(new Criteria().where("family").in(geoFamiliesId));
		}
		
		if ((fromAge != null) && (toAge != null)) {		

			Date now = new Date();
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.YEAR, -fromAge);
			Date fromDate = cal.getTime();
			
			cal.setTime(now);
			cal.add(Calendar.YEAR, -toAge);
			Date toDate = cal.getTime();
			
			criterias.add(new Criteria().andOperator(Criteria.where("birthday").lte(fromDate),Criteria.where("birthday").gte(toDate)));		
		}
		
		if (languages != null) {			
			criterias.add(new Criteria().where("languages").in(Arrays.asList(languages)));
		}
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));		
		List<Member> members = mongoTemplate.find(new Query(c), Member.class, "Member");
		
		Set<String> familiesId = new HashSet<String>();
		for (Member member : members) {
			familiesId.add(member.getFamily());
		}
		
		return familiesId;
	}
	
}
