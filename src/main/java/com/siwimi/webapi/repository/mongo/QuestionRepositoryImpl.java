package com.siwimi.webapi.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Question;
import com.siwimi.webapi.repository.QuestionRepositoryCustom;

@Repository
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@SuppressWarnings("static-access")
	@Override
	public List<Question> queryQuestion(String creatorId,Double longitude,Double latitude,String qsDistance,String queryText) {
				
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDeletedRecord").is(false));
	
		if (creatorId != null) {
			criterias.add(new Criteria().where("creator").is(creatorId));
		}
		
		if (queryText != null) {
			criterias.add(new Criteria().orOperator(Criteria.where("title").regex(queryText.trim(), "i"),
					                                Criteria.where("description").regex(queryText.trim(), "i")));
		}
		
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
		Query q = new Query(c);
		q = q.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"createdDate"));
		
		return mongoTemplate.find(q, Question.class, "Question");
	}
		
	@Override
	public Question saveQuestion(Question newQuestion) {		
		mongoTemplate.indexOps(Question.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newQuestion, "Question");
		return newQuestion;
	}
}
