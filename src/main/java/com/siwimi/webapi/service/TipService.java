package com.siwimi.webapi.service;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Location;
import com.siwimi.webapi.domain.Tip;
import com.siwimi.webapi.repository.FavoriteRepository;
import com.siwimi.webapi.repository.LocationRepository;
import com.siwimi.webapi.repository.TipRepository;

@Service
public class TipService {

	@Autowired
	private TipRepository tipRep;
	
	@Autowired
	private FavoriteRepository favoriteRep;	
	
	@Autowired
	private LocationRepository locationRep;
	
	public List<Tip> findTips(String creatorId, 
							  String requesterId,
			                  String status, String type, 
			                  Double longitude, Double latitude, String qsDistance, 
			                  String queryText,
							  Integer page,
							  Integer per_page) {
		
		List<Tip> tipList = tipRep.queryTip(creatorId, status, type, 
											longitude, latitude, qsDistance, queryText,
                                            page,per_page);
		
		for (int i=0; i<tipList.size(); i++) {
			Tip tip = tipList.get(i);
			// Populate isFavorite
			if (favoriteRep.queryFavorite(requesterId, tip.getId(), "tip") != null) {
				tip.setIsFavorite(true);
			}
			tipList.set(i, tip);
			// increment viewcount by 1, and save it to MongoDB
			tip.setViewCount(tip.getViewCount()+1);
			tipRep.saveTip(tip);
		}
		
		return tipList;
	}
	
	public Tip findByTipId(String id) {
		return tipRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Tip addTip(Tip newTip) {
		newTip.setIsDeletedRecord(false);
		newTip.setViewCount(0);
		newTip = updateLocation(newTip);
		return tipRep.saveTip(newTip);
	}
	
	public Tip updateTip(String id, Tip updatedTip) {
		updatedTip.setId(id);
		updatedTip = updateLocation(updatedTip);
		return tipRep.saveTip(updatedTip);
	}
	
	public void deleteTip(String id) {
		Tip tip = tipRep.findOne(id);
		tip.setIsDeletedRecord(true);
		tipRep.saveTip(tip);
	}
	
	public Tip updateLocation(Tip tip) {
		// lookup location from the collection Location;
		Location thisLocation = locationRep.queryLocation(tip.getZipCode(), tip.getCity(), tip.getState());
		// set longitude and latitude 
		if (thisLocation!=null) {
			double[] location = {thisLocation.getLongitude(), thisLocation.getLatitude()};
			tip.setZipCode(thisLocation.getZipCode());
			tip.setLocation(location);
			tip.setCity(thisLocation.getTownship());
			tip.setState(thisLocation.getStateCode());
		}

		return tip;
	}
	
	public String urlContent(String url) {
		String header = url;
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			header = doc.title();
		} catch (IOException e) {
			//e.printStackTrace();
		} 
				
		return header;
	}
}
