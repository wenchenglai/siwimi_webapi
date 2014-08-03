package com.adarp.xiwami.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Item;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.repository.ItemRepository;
import com.adarp.xiwami.repository.MemberRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRep;
	
	@Autowired
	private MemberRepository memberRep;
	
	@Autowired
	private FamilyRepository familyRep;
	
	public List<Item> FindItems(String sellerId,String status,Double longitude,Double latitude,String qsDistance,String queryText) {
		if ((sellerId!=null) && (status!=null))
			return itemRep.findBySellerAndStatusAndIsDeletedIsFalse(sellerId, status);
		else {
			// Geo search in family collection				
			String [] parts = qsDistance.split(" ");
			Distance distance;
			if (parts[1].toLowerCase().contains("mile"))
				distance = new Distance(Double.parseDouble(parts[0]),Metrics.MILES);
			else
				distance = new Distance(Double.parseDouble(parts[0]),Metrics.KILOMETERS);			
			List<Family> geoFamilies = familyRep.findByLocationNearAndIsDeletedIsFalse(new Point(longitude,latitude),distance);
			
			// Retrieve id of the members of geoFamilies
			List<String> geoMemberId = new ArrayList<String>();
			for (Family family : geoFamilies) {
				for (String memberId : family.getMembers()) {
					geoMemberId.add(memberId);
				}
			}
					
			return itemRep.findBySellerInAndNameDescriptionLikeIgnoreCaseAndIsDeletedIsFalse(geoMemberId, queryText);
		}
	}
	
	public Item FindByItemId(String id) {
		return itemRep.findOne(id);
	}
	
	public Item AddItem(Item newItem) {
		newItem.setIsDeleted(false);
		itemRep.save(newItem);
		
		//update items for the member
		Member member = memberRep.findOne(newItem.getSeller());
		List<String> memberItem = member.getItems();
		memberItem.add(newItem.getId());
		memberRep.save(member);
		
		return newItem;
	}
	
	public Item UpdateItem(String id, Item updatedItem) {
		updatedItem.setId(id);
		return itemRep.save(updatedItem);
	}
	
	public void DeleteItem(String id) {
		Item item = itemRep.findOne(id);
		item.setIsDeleted(true);
		itemRep.save(item);
	}
}
