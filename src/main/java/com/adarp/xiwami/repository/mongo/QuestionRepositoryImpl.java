package com.adarp.xiwami.repository.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import com.adarp.xiwami.repository.QuestionRepositoryCustom;
import com.adarp.xiwami.domain.Question;


@Repository
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Question> GetQuestions() throws Exception {
		
		return (mongoTemplate.findAll(Question.class, "Question"));
	}	
	
	@Override
	public Question GetQuestionById(String id) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(id));	
		return mongoTemplate.findOne(myQuery, Question.class, "Question");				
	}		
	
	@Override
	public void AddQuestion(Question newQuestion) throws Exception {
		
		mongoTemplate.save(newQuestion,"Question");
	}
	
	@Override
	public void UpdateQuestion(String id, Question updatedQuestion) throws Exception {
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(id));		
		DBObject updatedQuestionDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(updatedQuestion);
		updatedQuestionDBObject.removeField("_id");
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedQuestionDBObject));
		mongoTemplate.updateFirst(myQuery, setUpdate, Question.class, "Question");
	}

	@Override
	public void DeleteQuestion(String id) throws Exception {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("_id").is(id));
//		mongoTemplate.remove(query, Question.class, "Question");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoTemplate.updateFirst(query, Update.update("isDeleted", "Y"), Question.class, "Question");		
	}	
}
