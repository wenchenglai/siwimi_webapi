package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Feed;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.FeedService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.FeedSideload;
import com.siwimi.webapi.web.dto.FeedSideloadList;

@RestController
public class FeedController {
	
	@Autowired
	private FeedService feedService;

	@Autowired
	private MemberService memberService;
	
	// Get feeds by criteria
		@RequestMapping(value = "/feeds", method = RequestMethod.GET, produces = "application/json")
		public FeedSideloadList findFeeds(
				@RequestParam(value="requester", required=false) String requesterId,
				@RequestParam(value="longitude", required=false) Double longitude,
				@RequestParam(value="latitude", required=false) Double latitude) {
			FeedSideloadList responseBody = new FeedSideloadList();
			List<Feed> feedList = feedService.findFeeds(requesterId,longitude,latitude);
			
			Set<Member> members = new HashSet<Member>();
			if (feedList!=null) {			
				for (Feed feed : feedList) {
				Member member = memberService.findByMemberId(feed.getCreator());
				// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
				if (member!=null)
					members.add(member);
				}
			} else {
				// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
				feedList = new ArrayList<Feed>();
			}
			responseBody.feeds = feedList;
			responseBody.members = new ArrayList<Member>(members);

			return responseBody;
		}
		
	// Get Feed by ID
	@RequestMapping(value = "/feeds/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Feed> findByFeedId(@PathVariable("id") String id) {
		Map<String,Feed> responseBody = new HashMap<String,Feed>();			
		Feed Feed = feedService.findByFeedId(id);
		responseBody.put("Feed", Feed);
		return responseBody;
	}
	
	// Add New Feed
	@RequestMapping(value = "/feeds", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Feed> addFeed(@RequestBody FeedSideload newFeed) {
		Feed savedFeed = feedService.addFeed(newFeed.feed);		
		Map<String, Feed> responseBody = new HashMap<String, Feed>();
		responseBody.put("Feed", savedFeed);		
		return responseBody;		
	}	
	
	// Update Feed
	@RequestMapping(value = "/feeds/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Feed> updateFeed(@PathVariable("id") String id, @RequestBody FeedSideload updatedFeed) {
		Feed savedFeed = feedService.updateFeed(id, updatedFeed.feed);
		Map<String, Feed> responseBody = new HashMap<String, Feed>();
		responseBody.put("Feed", savedFeed);		
		return responseBody;			
	}
	
	// Delete Feed
	@RequestMapping (value = "/feeds/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteFeed(@PathVariable("id")String id) {
		feedService.deleteFeed(id);
	}	
}
