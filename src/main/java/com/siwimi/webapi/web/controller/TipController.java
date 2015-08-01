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

import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.domain.Tip;
import com.siwimi.webapi.service.FeedbackService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.service.TipService;
import com.siwimi.webapi.web.dto.PreviewWebPage;
import com.siwimi.webapi.web.dto.TipSideload;
import com.siwimi.webapi.web.dto.TipSideloadList;

@RestController
public class TipController {

	@Autowired
	private TipService tipService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	// Get tips by type
	@RequestMapping(value = "/tips", method = RequestMethod.GET, produces = "application/json")
	public TipSideloadList findTips(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="requester", required=false) String requesterId, // userId who is sending this query request			
			@RequestParam(value="status", required=false) String status,	// *** popular (by vote)*** status is transient, depends on expirationDate.  They are {all, popular, active, expired}.  Popular depends on the up votes.
			@RequestParam(value="type", required=false) String type,		// type could be {all, deal, health, education, entertainment, misc}
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="distance", required=false) String qsDistance, 
			@RequestParam(value="queryText", required=false) String queryText,
			@RequestParam(value="pageNumber", required=false) Integer pageNumber, 
			@RequestParam(value="pageSize", required=false) Integer pageSize,
			@RequestParam(value="sortBy", required=false) String sortBy) {
		
		TipSideloadList responseBody = new TipSideloadList();
		List<Tip> tipList = tipService.findTips(creatorId, requesterId, status, type, 
											    longitude, latitude, qsDistance, queryText,
                                                pageNumber,pageSize,sortBy);
		Set<Member> members = new HashSet<Member>();
		if (tipList!=null) {
			for (Tip tip : tipList) {
				Member member = memberService.findByMemberId(tip.getCreator());
				// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
				if (member!=null)
					members.add(member);
				// Populate replies
				List<Feedback> feedbacks = feedbackService.find(null, tip.getId(), "tip", null);
				if ((feedbacks!=null) && (!feedbacks.isEmpty())) {
					for (Feedback feedback : feedbacks) {
						List<String> replies = tip.getReplies();
						replies.add(feedback.getId());
						tip.setReplies(replies);
					}
				}
			}
		} else {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			tipList = new ArrayList<Tip>();
		}
		
		responseBody.tips = tipList;
		responseBody.members = new ArrayList<Member>(members);
		return responseBody;

	}
	
	// Get tip by ID
	@RequestMapping(value = "/tips/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Tip> findByTipId(@PathVariable("id") String id) {		
		Map<String,Tip> responseBody = new HashMap<String,Tip>();			
		Tip tip = tipService.findByTipId(id);
		responseBody.put("tip", tip);
		return responseBody;
	}
	
	// Add New tip
	@RequestMapping(value = "/tips", method = RequestMethod.POST, produces = "application/json")
	public Map<String,Tip> addTip(@RequestBody TipSideload newTip){
		Tip savedTip = tipService.addTip(newTip.tip);			
		Map<String,Tip> responseBody = new HashMap<String,Tip>();
		responseBody.put("tip", savedTip);
		return responseBody;
	}	
	
	// Update tip
	@RequestMapping(value = "/tips/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String,Tip> updateTip(@PathVariable("id") String id, @RequestBody TipSideload updatedTip) {
		Tip savedTip = tipService.updateTip(id,updatedTip.tip);		
		Map<String,Tip> responseBody = new HashMap<String,Tip>();
		responseBody.put("tip", savedTip);
		return responseBody;		
	}
	
	// Delete tip
	@RequestMapping (value = "/tips/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteTip(@PathVariable("id")String id) {
		tipService.deleteTip(id);
	}	
	
	// Get URL header
	@RequestMapping(value = "/tips/findURL", method = RequestMethod.GET, produces = "application/json")
	public PreviewWebPage findURL(@RequestParam(value="url", required=false) String url) {
		PreviewWebPage obj = new PreviewWebPage();
		obj.text = tipService.urlContent(url);
		return obj;
	}
}
