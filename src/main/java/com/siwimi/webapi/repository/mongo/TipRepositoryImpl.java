package com.siwimi.webapi.repository.mongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Count;
import com.siwimi.webapi.domain.Tip;
import com.siwimi.webapi.repository.TipRepositoryCustom;

@Repository
public class TipRepositoryImpl implements TipRepositoryCustom{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Tip> queryTip(String creatorId,
			                  String status, String type, 
			                  Double longitude, Double latitude, String qsDistance, 
			                  String queryText,Integer page, Integer per_page, String sortBy) {				

		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDeletedRecord").is(false));

		if (creatorId != null) {
			criterias.add(new Criteria().where("creator").is(creatorId));
		}
		
		if ((type != null) && (!type.equals("all"))) {	
			criterias.add(new Criteria().where("type").is(type));
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
		
		if ((status != null) && (!status.equals("all"))) {	
			if (status.equalsIgnoreCase("active")) {
				criterias.add(new Criteria().where("expiredDate").gt(new Date()));
			}
			
			if (status.equalsIgnoreCase("expired")) {
				criterias.add(new Criteria().where("expiredDate").lt(new Date()));
			}
		}

		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		
		List<Tip> allResults = mongoTemplate.find(new Query(c), Tip.class, "Tip");
		
		if ((allResults == null) || (allResults.isEmpty()))
			return new ArrayList<Tip>();
		else {
			
			int pageSize = 1000;
			if (per_page!=null)
				pageSize = per_page.intValue();
			
			int skip = 0;
			if (page!=null)
				skip = (page.intValue()-1)*pageSize;
			
			Query q = new Query(c).limit(pageSize).skip(skip);			
			if (sortBy != null) {
				if (sortBy.equals("title")) {
					q = q.with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"title").and(new Sort(Sort.DEFAULT_DIRECTION.ASC,"createdDate")));
				} else if (sortBy.equals("type")) {
					q = q.with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"type").and(new Sort(Sort.DEFAULT_DIRECTION.ASC,"createdDate")));
				} else {
					q = q.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"createdDate")
					                   .and(new Sort(Sort.DEFAULT_DIRECTION.DESC,"expiredDate")));
				}
			} else {
				q = q.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"createdDate").and(new Sort(Sort.DEFAULT_DIRECTION.DESC,"expiredDate")));
			}
			
			// Retrieve the queried candidate Tips 
			List<Tip> tipCandidateList = mongoTemplate.find(q, Tip.class, "Tip");
			if ((tipCandidateList == null) || (tipCandidateList.isEmpty())) {
				return new ArrayList<Tip>();
			}
			
			// Convert the candidated tips to map
			Map<String,Tip> tipCandidateMap = new LinkedHashMap<String,Tip>();
			for (Tip tip : tipCandidateList)
				tipCandidateMap.put(tip.getId(),tip);

			// Retrieve ID list from map
			List<String> tipCandidateIdList = new ArrayList<String>(tipCandidateMap.keySet());

			/******* Aggregation of VoteUp and VoteDown *******/
			// basicVoteCriterias : all, except voteType
			List<Criteria> basicVoteCriterias = new ArrayList<Criteria>();
			basicVoteCriterias.add(new Criteria().where("isDeletedRecord").is(false));
			basicVoteCriterias.add(new Criteria().where("objectType").regex("tip", "i"));
			basicVoteCriterias.add(new Criteria().where("targetObject").in(tipCandidateIdList));
			Criteria basicVoteCriteria = new Criteria().andOperator(basicVoteCriterias.
					                                                toArray(new Criteria[basicVoteCriterias.size()]));

			// add criteria : voteType == up
			Criteria upVoteCriteria = new Criteria().andOperator(basicVoteCriteria,
					                                             new Criteria().where("voteType").regex("up", "i"));
			// add criteria : voteType == down
			Criteria downVoteCriteria = new Criteria().andOperator(basicVoteCriteria,
					                                               new Criteria().where("voteType").regex("down", "i"));
			
			// Aggregation : voteType = up
			Aggregation upVoteAgg = Aggregation.newAggregation(
					Aggregation.match(upVoteCriteria),
					Aggregation.group("targetObject").count().as("count"));

			// upVoteCountList : Aggregation results of vote up
			AggregationResults<Count> upVoteAggResult = mongoTemplate.aggregate(upVoteAgg, "Vote", Count.class);
			List<Count> upVoteCountList = upVoteAggResult.getMappedResults();
			
			// upVoteResultMap : Aggregation results of vote up
			Map<String,Count> upVoteResultMap = new LinkedHashMap<String,Count>();
			for (Count count : upVoteCountList) {
				upVoteResultMap.put(count.getId(),count);
			}
			
			// Aggregation : voteType = down
			Aggregation downVoteAgg = Aggregation.newAggregation(
					Aggregation.match(downVoteCriteria),
			        Aggregation.group("targetObject").count().as("count"));

			// downVoteCountList : Aggregation results of vote down
			AggregationResults<Count> downVoteAggResult = mongoTemplate.aggregate(downVoteAgg, "Vote", Count.class);
			List<Count> downVoteResultList = downVoteAggResult.getMappedResults();	
			
			// downVoteResultMap : Aggregation results of vote down
			Map<String,Count> downVoteResultMap = new LinkedHashMap<String,Count>();
			for (Count count : downVoteResultList) {
				downVoteResultMap.put(count.getId(),count);
			}
			
			// Populate candidate tips with voteUp and voteDown
			for (int i = 0; i < tipCandidateList.size(); i++) {
				Tip tip = tipCandidateList.get(i);
				
				if (upVoteResultMap.containsKey(tip.getId())) {
					tip.setVoteUpCount(upVoteResultMap.get(tip.getId()).getCount());
				}
				
				if (downVoteResultMap.containsKey(tip.getId())) {
					tip.setVoteDownCount(downVoteResultMap.get(tip.getId()).getCount());
				}
				
				tip.setQueryCount(allResults.size());
			}
			
			if (status != null) {
				if (status.equalsIgnoreCase("popular")) {
					Collections.sort(tipCandidateList);
				} 
			}
			return tipCandidateList;	
		}
	}
	
	@Override
	public Tip saveTip(Tip newTip) {		
		mongoTemplate.indexOps(Tip.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newTip, "Tip");
		return newTip;
	}
}
