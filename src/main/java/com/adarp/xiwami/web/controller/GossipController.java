package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Gossip;
import com.adarp.xiwami.service.GossipService;

@RestController
public class GossipController {

	@Autowired
	private GossipService gossipService;
	
	// Get all gossips
	@RequestMapping(value = "/gossips", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Gossip>> FindGossips(
			@RequestParam(value="status", required=false) String status,	
			@RequestParam(value="userId", required=false) String user,
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude) {
		Map<String,List<Gossip>> responseBody = new HashMap<String,List<Gossip>>();
		List<Gossip> list = gossipService.FindGossips(status,user,longitude,latitude);
		responseBody.put("gossip", list);
		return responseBody;
	}
	
	
}
