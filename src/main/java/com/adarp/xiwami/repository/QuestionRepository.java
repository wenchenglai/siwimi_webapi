package com.adarp.xiwami.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.adarp.xiwami.domain.Question;

public interface QuestionRepository extends MongoRepository<Question, String>, QuestionRepositoryCustom{
	List<Question> findByCreatorAndStatusAndIsDeletedIsFalse(String creatorId,String status);
	
	@Query("{'$and':[ {'isDeleted' : false}, {'$or' : [{'questionText':{$regex : ?0, $options :'i'}}, {'answers' : {'$in' :[{$regex : ?0, $options :'i'}]}}] } ] }")
	List<Question> findByQuestionTextLikeIgnoreCaseAndAnswersLikeIgnoreCaseAndIsDeletedIsFalse(String queryText);

	

}
