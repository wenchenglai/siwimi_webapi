package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.web.dto.FamilySideload;

@Service
public class FamilyService {

	@Autowired
	private FamilyRepository familyRep;
	
	public List<Family> FindFamilies() {
		try {
			return familyRep.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to query Families.");
			return null;
		}
	}
	
	public Family FindByFamilyId(String id) {
		try {
			return familyRep.findOne(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to query Family.");
			return null;
		}
	}
	
	public void AddFamily(FamilySideload newFamily) {
		try {
			familyRep.AddFamily(newFamily.family);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");
		}

	}
	
	public void EditFamily(String id,FamilySideload updatedFamily) {
		try {
			updatedFamily.family.set_Id(id);
			familyRep.UpdateFamily(updatedFamily.family);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to update Family.");
		}
	}
	
	public void DeleteFamily(String id) {
		try {
			familyRep.DeleteFamily(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to delete Family.");			
		}
	}
	
	public List<Family> SearchFamilyNearby(String id, double distance) {
		Family thisFamily = familyRep.findOne(id);
		double[] coordinates = thisFamily.getLocation();	
		Point point = new Point(coordinates[0],coordinates[1]);
		return familyRep.findByLocationNear(point,new Distance(distance,Metrics.MILES));
	}
}
