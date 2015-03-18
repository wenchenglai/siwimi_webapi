package com.adarp.xiwami.web.controller;

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

import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.domain.Tip;
import com.adarp.xiwami.service.MemberService;
import com.adarp.xiwami.service.TipService;
import com.adarp.xiwami.web.dto.TipSideload;
import com.adarp.xiwami.web.dto.PreviewWebPage;
import com.adarp.xiwami.web.dto.TipSideloadList;

@RestController
public class TipController {

	@Autowired
	private TipService tipService;
	
	@Autowired
	private MemberService memberService;
	
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
			@RequestParam(value="queryText", required=false) String queryText) {
		
		TipSideloadList responseBody = new TipSideloadList();
		List<Tip> tipList = tipService.findTips(creatorId, requesterId, status, type, longitude, latitude, qsDistance, queryText);
		Set<Member> members = new HashSet<Member>();
		if (tipList!=null) {
			for (Tip tip : tipList) {
				Member member = memberService.findByMemberId(tip.getCreator());
				if (member!=null)
					members.add(member);
				else
					// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
					members.add(new Member());
			}
		} else {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			tipList = new ArrayList<Tip>();
		}
		
		/*
		List<Tip> tipList = null;
		
		try {
			tipList = tipService.findTips(creatorId, requesterId, status, type, longitude, latitude, qsDistance, queryText);
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			tipList = new ArrayList<Tip>();

		}
		responseBody.put("tip", tipList); 		*/
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
