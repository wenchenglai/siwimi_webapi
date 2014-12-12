package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Tip;
import com.adarp.xiwami.service.TipService;

@RestController
public class TipController {

	@Autowired
	private TipService tipService;
	
	// Get all tips
	@RequestMapping(value = "/tips", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Tip>> FindGossips(
			@RequestParam(value="status", required=false) String status,	
			@RequestParam(value="userId", required=false) String user,
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude) {
		Map<String,List<Tip>> responseBody = new HashMap<String,List<Tip>>();
		List<Tip> list = tipService.FindGossips(status,user,longitude,latitude);
		responseBody.put("tip", list);
		return responseBody;
	}
	
	
}
