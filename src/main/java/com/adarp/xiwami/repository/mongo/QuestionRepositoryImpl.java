package com.adarp.xiwami.repository.mongo;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Question;
import com.adarp.xiwami.repository.QuestionRepositoryCustom;

@Repository
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Question> queryQuestion(String creatorId,Double longitude,Double latitude,String qsDistance,String queryText) {
				
		Criteria c = new Criteria();
		c = Criteria.where("isDeleted").is(false);
	
		if (creatorId != null) {
			c = c.andOperator(Criteria.where("creator").is(creatorId));
		}
		
		if (queryText != null) {
			c = c.orOperator(Criteria.where("title").regex(queryText.trim(), "i"),
					         Criteria.where("description").regex(queryText.trim(), "i"),
					         Criteria.where("answers").in(Pattern.compile("(?)"+queryText.trim())));
		}
		
		if ((longitude != null) && (latitude != null) && (qsDistance!= null)) {
			
			double distance = 0.0;
			String [] parts = qsDistance.split(" ");
			if (parts[1].toLowerCase().contains("mile"))
				distance = Double.parseDouble(parts[0])/3959;
			else	
				distance = Double.parseDouble(parts[0])/6371;
					
			c = c.andOperator(Criteria.where("location").nearSphere(new Point(longitude,latitude)).maxDistance(distance));
		}
		
		return mongoTemplate.find(new Query(c), Question.class, "Question");
	}
		
	@Override
	public Question saveQuestion(Question newQuestion) {		
		mongoTemplate.indexOps(Question.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newQuestion, "Question");
		return newQuestion;
	}
}
