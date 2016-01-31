package com.siwimi.webapi.repository.mongo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.repository.ActivityRepositoryCustom;

@Repository
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@SuppressWarnings("static-access")
	@Override
	public List<Activity> queryActivity(String creatorId,String status,String type,Integer period,String fromTime, String toTime,
			                            Double longitude,Double latitude,String qsDistance,String ageGroup,String stage,
			                            boolean isFree,String queryText,
			                            Integer page, Integer per_page, String sortBy) {
			
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
			
			// 2016-01-13 temporary commented out due to changes in distance filter design
//			String [] parts = qsDistance.split(" ");
//			if (parts[1].toLowerCase().contains("mile"))
//				distance = Double.parseDouble(parts[0])/3959;
//			else	
//				distance = Double.parseDouble(parts[0])/6371;
			
			if (!qsDistance.equals("all")) {
				distance = Double.parseDouble(qsDistance)/3959;		
				criterias.add(new Criteria().where("location").nearSphere(new Point(longitude,latitude)).maxDistance(distance));
			}
		}
		
		if (ageGroup != null) {
			if (ageGroup.equals("infant"))
				criterias.add(new Criteria().andOperator(Criteria.where("fromAge").lte(1),
						                                 Criteria.where("toAge").gte(0)));
			else if (ageGroup.equals("toddler"))
				criterias.add(new Criteria().andOperator(Criteria.where("fromAge").lte(3),
                        								 Criteria.where("toAge").gte(2)));			
			else if (ageGroup.equals("pre-school"))
				criterias.add(new Criteria().andOperator(Criteria.where("fromAge").lte(5),
                        								 Criteria.where("toAge").gte(4)));
			else if (ageGroup.equals("school-age"))
				criterias.add(new Criteria().andOperator(Criteria.where("fromAge").lte(11),
                        								 Criteria.where("toAge").gte(6)));
			else if (ageGroup.equals("pre-teen"))
				criterias.add(new Criteria().andOperator(Criteria.where("fromAge").lte(14),
                        								 Criteria.where("toAge").gte(12)));
		}
		
		if (stage != null) {
			criterias.add(new Criteria().where("stage").is(stage));
		}
		
		if (isFree) {
			criterias.add(new Criteria().andOperator(Criteria.where("price").is(0)));
		}
		
		if (status != null) {		
			Date now = new Date();
			if (status.equals("past")) {
				criterias.add(new Criteria().where("toDate").lt(now));
			} else if (status.equals("current")) {
				Criteria c1 = new Criteria().andOperator(Criteria.where("fromDate").lte(now),
                        								 Criteria.where("toDate").gte(now));
				Criteria c2 = new Criteria().andOperator(Criteria.where("fromDate").lte(now),
														 Criteria.where("toDate").is(null),
														 Criteria.where("fromDate").gt(shiftDateWithoutTime(now,-1)));
				Criteria c3 = new Criteria().andOperator(Criteria.where("fromDate").is(null),
														 Criteria.where("toDate").gte(now),
						 								 Criteria.where("toDate").lt(shiftDateWithoutTime(now,1)));
				criterias.add(new Criteria().orOperator(c1,c2,c3));
			} else if (status.equals("upcoming")){
				criterias.add(new Criteria().where("fromDate").gte(now));
			} else if (status.equals("timeless")) {
				criterias.add(new Criteria().andOperator(Criteria.where("fromDate").is(null),
                                                         Criteria.where("toDate").is(null)));
			} else if (status.equals("all")) {
				// only list current + future events
				Criteria c1 = new Criteria().where("toDate").gte(now);
				Criteria c2 = new Criteria().where("fromDate").gte(now);
				criterias.add(new Criteria().orOperator(c1,c2));
			}
		}
	
		if ((type != null) && (!type.equals("all"))) {	
			criterias.add(new Criteria().where("type").is(type));
		}
		
		if (period != null) {
			Date now = new Date();
			Date periodBegin = null;
			Date periodEnd = null;
			
			switch (period.intValue()) {
				case 1: // Today
					periodBegin = shiftDateWithoutTime(now,0);
					periodEnd = shiftDateWithoutTime(now,1);
					break;
				case 2: // This coming weekend
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					Date thisWeekend = c.getTime();
					periodBegin = shiftDateWithoutTime(thisWeekend,0); 
					periodEnd = shiftDateWithoutTime(thisWeekend,2);
					break;
				case 3: // next 30 days
					periodBegin = shiftDateWithoutTime(now,0);
					periodEnd = shiftDateWithoutTime(now,30);
					break;
				case 4: // next 6 months
					periodBegin = shiftDateWithoutTime(now,0);
					periodEnd = shiftDateWithoutTime(now,180);
					break;	
				case 5: // custom time range
					SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z '('z')'");
					try {
						if (fromTime != null)
							periodBegin = shiftDateWithoutTime(format.parse(fromTime),0);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					try {
						if (toTime != null)
							periodEnd = shiftDateWithoutTime(format.parse(toTime),0);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					break;
				default: //today
					periodBegin = shiftDateWithoutTime(now,0);
					periodEnd = shiftDateWithoutTime(now,1);
					break;
			}					
			
			if ((periodBegin!=null) && (periodEnd!=null)) {
				// Both fromDate and toDate are not null
				Criteria c1 = new Criteria().andOperator(Criteria.where("fromDate").lte(periodEnd),
                                                         Criteria.where("toDate").gte(periodBegin));
				// toDate is null
				Criteria c2 = new Criteria().andOperator(Criteria.where("fromDate").lte(periodEnd),
						                                 Criteria.where("fromDate").gte(periodBegin),
                                                         Criteria.where("toDate").is(null));
				// fromDate is null
				Criteria c3 = new Criteria().andOperator(Criteria.where("toDate").lte(periodEnd),
						                                 Criteria.where("toDate").gte(periodBegin),
                                                         Criteria.where("fromDate").is(null));
				// Both fromDate and toDate are null
				Criteria c4 = new Criteria().andOperator(Criteria.where("fromDate").is(null),
                                                         Criteria.where("toDate").is(null));
				criterias.add(new Criteria().orOperator(c1,c2,c3,c4));
			}

			if ((periodBegin!=null) && (periodEnd==null))
				criterias.add(new Criteria().andOperator(Criteria.where("fromDate").is(null),
						                                 Criteria.where("toDate").gte(periodBegin)));
			if ((periodBegin==null) && (periodEnd!=null))
				criterias.add(new Criteria().andOperator(Criteria.where("fromDate").lte(periodEnd),
						 								 Criteria.where("toDate").is(null)));
			if ((periodBegin==null) && (periodEnd==null))
				criterias.add(new Criteria().andOperator(Criteria.where("fromDate").is(null),
                                                         Criteria.where("toDate").is(null)));			
		}
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));

		// Queried result without pagination
		List<Activity> allResults = mongoTemplate.find(new Query(c), Activity.class, "Activity");
		
		if ((allResults == null) || (allResults.isEmpty()))
			return new ArrayList<Activity>();
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
					q = q.with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"title").and(new Sort(Sort.DEFAULT_DIRECTION.DESC,"createdDate")));
				} else if (sortBy.equals("type")) {
					q = q.with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"type").and(new Sort(Sort.DEFAULT_DIRECTION.DESC,"createdDate")));
				} else {
					q = q.with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"fromDate")
					                   .and(new Sort(Sort.DEFAULT_DIRECTION.ASC,"createdDate")));
				}
			} else {
				if ((status!=null) && (status.equals("past")))
					q = q.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"fromDate").and(new Sort(Sort.DEFAULT_DIRECTION.DESC,"createdDate")));
				else
					q = q.with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"fromDate").and(new Sort(Sort.DEFAULT_DIRECTION.ASC,"createdDate")));
			}
		
			// Queried result with pagination
			List<Activity> queryResults = mongoTemplate.find(q, Activity.class, "Activity");
			
			// Insert total record count to the first element of the queried result
			if ((queryResults == null) || (queryResults.isEmpty())) {
				return new ArrayList<Activity>();
			} else {
				Activity activity = queryResults.get(0);
				activity.setQueryCount(allResults.size());
			}
				
			return queryResults;
		}					
	}
	
	@SuppressWarnings("static-access")
	@Override
	public Boolean isExisted(String customData, String parser) {
		if (customData == null) {
			return false;
		}
		
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDeletedRecord").is(false));
		criterias.add(new Criteria().where("customData").is(customData));
		criterias.add(new Criteria().where("parser").is(parser));
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		
		Activity one = mongoTemplate.findOne(new Query(c), Activity.class, "Activity");
		
		return one != null;
	}
		
	// Need to create geospatial index for events before saving
	@Override
	public Activity saveActivity(Activity newActivity) {
		mongoTemplate.indexOps(Activity.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newActivity, "Activity");		
		return newActivity;
	}	
	
	// Remove HH:MM:ss from Date object
	// if shift = 1, return tomorrow.
	public Date shiftDateWithoutTime(Date date, int shift) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    calendar.add(Calendar.DATE, shift);

	    return calendar.getTime();
	}

}
