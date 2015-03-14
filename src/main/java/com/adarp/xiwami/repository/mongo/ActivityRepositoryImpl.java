package com.adarp.xiwami.repository.mongo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Activity;
import com.adarp.xiwami.repository.ActivityRepositoryCustom;

@Repository
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@SuppressWarnings("static-access")
	@Override
	public List<Activity> queryActivity(String creatorId,String status,String type,Integer period,String fromTime, String toTime,
			                            Double longitude,Double latitude,String qsDistance,String queryText) {
			
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDestroyed").is(false));
	
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
		
		if ((status != null) && (!status.equals("all"))) {		
			Date now = new Date();
			if (status.equalsIgnoreCase("Past")) {
				criterias.add(new Criteria().where("toTime").lt(now));
			} else if (status.equalsIgnoreCase("Ongoing")) {
				criterias.add(new Criteria().andOperator(Criteria.where("fromTime").lte(now),Criteria.where("toTime").gte(now)));
			}
			else if (status.equalsIgnoreCase("Upcoming")){
				criterias.add(new Criteria().where("fromTime").gt(now));
			}			
		}
	
		if ((type != null) && (!type.equals("all"))) {	
			criterias.add(new Criteria().where("type").is(type));
		}
		
		if (period != null) {
			Date now = new Date();
			Date periodBegin = new Date();
			Date periodEnd = new Date();
			
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
						periodBegin = shiftDateWithoutTime(format.parse(fromTime),0);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					try {
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
			criterias.add(new Criteria().andOperator(Criteria.where("fromTime").lte(periodEnd),
                    								 Criteria.where("toTime").gte(periodBegin)));
		}
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.find(new Query(c), Activity.class, "Activity");
	}
		
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
