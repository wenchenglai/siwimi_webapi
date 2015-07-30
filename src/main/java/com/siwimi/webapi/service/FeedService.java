package com.siwimi.webapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.domain.Feed;
import com.siwimi.webapi.domain.Item;
import com.siwimi.webapi.domain.Question;
import com.siwimi.webapi.domain.Tip;
import com.siwimi.webapi.repository.ActivityRepository;
import com.siwimi.webapi.repository.FeedRepository;
import com.siwimi.webapi.repository.ItemRepository;
import com.siwimi.webapi.repository.QuestionRepository;
import com.siwimi.webapi.repository.TipRepository;

@Service
public class FeedService {

	@Autowired
	private FeedRepository feedRep;
	
	@Autowired
	private ActivityRepository activityRep;
	
	@Autowired
	private QuestionRepository questionRep;
	
	@Autowired
	private TipRepository tipRep;
	
	@Autowired
	private ItemRepository itemRep;
	
	public List<Feed> findFeeds(String requesterId, Double longitude, Double latitude) {
		
		List<Feed> feeds = null;
		
		// For each types : only lists five elements
		Integer page = new Integer(0);
		Integer per_page = new Integer(5);
		
		List<Feed> feedList = feedRep.query(requesterId);
		if ((feedList != null) && (!feedList.isEmpty()))
			feeds = new ArrayList<Feed>(feedList.subList(0, feedList.size()<5 ? feedList.size() : 5));
		else
			feeds = new ArrayList<Feed>();
				
		// Convert Activities (Events) to Feeds
		List<Activity> activities = activityRep.queryActivity(null,"upcoming",null,null,null,null,
				                                              longitude,latitude,"15 mile",null,page,per_page,null);
		if ((activities != null) && (!activities.isEmpty())) {
			for (Activity activity : activities.subList(0, activities.size()<5 ? activities.size() : 5)) {
				Feed feed = new Feed();
				feed.setcId(activity.getId());
				feed.setType("activity");
				feed.setCreator(activity.getCreator());
				feed.setTitle(activity.getTitle());
				feed.setDescription(activity.getDescription());
				feed.setCreatedDate(activity.getCreatedDate());
				feeds.add(feed);
			}	
		}
		
		// Convert Questions to Feeds
		List<Question> questions = questionRep.queryQuestion(requesterId,longitude,latitude,"15 mile",null);
		if ((questions != null) && (!questions.isEmpty())) { 
			for (Question question : questions.subList(0, questions.size()<5 ? questions.size() : 5)) {
				Feed feed = new Feed();
				feed.setcId(question.getId());
				feed.setType("question");
				feed.setCreator(question.getCreator());
				feed.setTitle(question.getTitle());
				feed.setDescription(question.getDescription());
				feed.setCreatedDate(question.getCreatedDate());
				feeds.add(feed);
			}
		}

		// Convert Tips to Feeds
		List<Tip> tips = tipRep.queryTip(null,null, null, longitude,latitude,"15 mile",null,page,per_page,null);
		if ((tips != null) && (!tips.isEmpty())) { 
			for (Tip tip : tips.subList(0, tips.size()<5 ? tips.size() : 5)) {
				Feed feed = new Feed();
				feed.setcId(tip.getId());
				feed.setType("tip");
				feed.setCreator(tip.getCreator());
				feed.setTitle(tip.getTitle());
				feed.setDescription(tip.getDescription());
				feed.setCreatedDate(tip.getCreatedDate());
				feeds.add(feed);
			}
		}	
		
		// Convert Items to Feeds
		List<Item> items = itemRep.queryItem(null,null, null, null, longitude,latitude,"15 mile",null,page,per_page,null);       
		if ((items != null) && (!items.isEmpty())) { 
			for (Item item : items.subList(0, items.size()<5 ? items.size() : 5)) {
				Feed feed = new Feed();
				feed.setcId(item.getId());
				feed.setType("item");
				feed.setCreator(item.getCreator());
				feed.setTitle(item.getTitle());
				feed.setDescription(item.getDescription());
				feed.setCreatedDate(item.getCreatedDate());
				feeds.add(feed);
			}
		}	
		return feeds;
	}
	
	public Feed findByFeedId(String id) {
		return feedRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Feed addFeed(Feed newFeed){
		newFeed.setIsDeletedRecord(false);
		return feedRep.save(newFeed);
	}
	
	public Feed updateFeed(String id, Feed updatedFeed){
		updatedFeed.setId(id);
		return feedRep.save(updatedFeed);
	}
	
	public void deleteFeed(String id){
		Feed feed = feedRep.findOne(id);
		feed.setIsDeletedRecord(true);
		feedRep.save(feed);
	}
}
