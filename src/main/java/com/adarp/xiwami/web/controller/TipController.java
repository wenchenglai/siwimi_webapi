package com.adarp.xiwami.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Tip;
import com.adarp.xiwami.service.TipService;
import com.adarp.xiwami.web.dto.TipSideload;

@RestController
public class TipController {

	@Autowired
	private TipService tipService;
	
	// Get tips by type
	@RequestMapping(value = "/tips", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Tip>> FindTips(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="status", required=false) String status,	
			@RequestParam(value="type", required=false) String type,				
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="distance", required=false) String qsDistance, 
			@RequestParam(value="queryText", required=false) String queryText) {
		
		Map<String,List<Tip>> responseBody = new HashMap<String,List<Tip>>();
		List<Tip> tipList = null;
		try {
			tipList = tipService.findTips(status, type, longitude, latitude, qsDistance, queryText);
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			tipList = new ArrayList<Tip>();

		}
		responseBody.put("tip", tipList);
		return responseBody;
	}
	
	// Get tip by ID
	@RequestMapping(value = "/tips/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Tip> FindByTipId(@PathVariable("id") String id) {		
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
	
	// Delete Item
	@RequestMapping (value = "/tips/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteTip(@PathVariable("id")String id) {
		tipService.deleteTip(id);
	}	
}
