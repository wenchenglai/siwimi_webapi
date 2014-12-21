package com.adarp.xiwami.web.controller;

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
			@RequestParam(value="type", required=true) String type) {
		Map<String,List<Tip>> responseBody = new HashMap<String,List<Tip>>();
		List<Tip> tipList = tipService.FindByType(type);
		responseBody.put("tip", tipList);
		return responseBody;
	}
	
	// Get tip by ID
	@RequestMapping(value = "/tips/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Tip> FindByTipId(@PathVariable("id") String id) {		
		Map<String,Tip> responseBody = new HashMap<String,Tip>();			
		Tip tip = tipService.FindByTipId(id);
		responseBody.put("tip", tip);
		return responseBody;
	}
	
	// Add New tip
	@RequestMapping(value = "/tips", method = RequestMethod.POST, produces = "application/json")
	public Map<String,Tip> AddTip(@RequestBody TipSideload newTip){
		Tip savedTip = tipService.AddTip(newTip.tip);			
		Map<String,Tip> responseBody = new HashMap<String,Tip>();
		responseBody.put("tip", savedTip);
		return responseBody;
	}	
	
	// Update tip
	@RequestMapping(value = "/tips/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String,Tip> UpdateTip(@PathVariable("id") String id, @RequestBody TipSideload updatedTip) {
		Tip savedTip = tipService.UpdateTip(id,updatedTip.tip);		
		Map<String,Tip> responseBody = new HashMap<String,Tip>();
		responseBody.put("tip", savedTip);
		return responseBody;		
	}
	
	// Delete Item
	@RequestMapping (value = "/tips/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteTip(@PathVariable("id")String id) {
		tipService.DeleteTip(id);
	}
	
}
