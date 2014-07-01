package com.adarp.xiwami.repository;

import com.adarp.xiwami.domain.Activity;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ActivityRepository {
	public List<Activity> GetActivities() throws Exception;
	
	public Activity GetActivityById(String id) throws Exception;
	
	public void AddActivity(Activity newActivity) throws Exception;
	
	public void UpdateActivity(String id, Activity updateActivity) throws Exception;
	
	public void DeleteActivity(String id) throws Exception;		
}
