package com.adarp.xiwami.service;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Tip;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.FavoriteRepository;
import com.adarp.xiwami.repository.TipRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class TipService {

	@Autowired
	private TipRepository tipRep;
	
	@Autowired
	private FavoriteRepository favoriteRep;	
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
	public List<Tip> findTips(String creatorId, 
							  String requesterId,
			                  String status, String type, 
			                  Double longitude, Double latitude, String qsDistance, 
			                  String queryText) {
		
		List<Tip> tipList = tipRep.queryTip(creatorId, status, type, longitude, latitude, qsDistance, queryText);
		
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
		return tipRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Tip addTip(Tip newTip) {
		newTip.setIsDestroyed(false);
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
		tip.setIsDestroyed(true);
		tipRep.saveTip(tip);
	}
	
	public Tip updateLocation(Tip tip) {
		// lookup zipcode from the collection ZipCode;
		ZipCode thisZipCode = zipCodeRep.queryZipCode(tip.getZipCode(), tip.getCity(), tip.getState());
		// set longitude and latitude 
		if (thisZipCode!=null) {
			double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
			tip.setZipCode(thisZipCode.getZipCode());
			tip.setLocation(location);
			tip.setCity(thisZipCode.getTownship());
			tip.setState(thisZipCode.getStateCode());
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
