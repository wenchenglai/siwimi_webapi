package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Tip;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.TipRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class TipService {

	@Autowired
	TipRepository tipRep;
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
	public List<Tip> findTips(String status, String type, Double longitude, Double latitude, String qsDistance, String queryText) {
		return tipRep.queryTip(status, type, longitude, latitude, qsDistance, queryText);	
	}
	
	public Tip findByTipId(String id) {
		return tipRep.findOne(id);
	}
	
	public Tip addTip(Tip newTip) {
		newTip.setIsDeleted(false);
		newTip = updateZipCode(newTip);
		return tipRep.saveTip(newTip);
	}
	
	public Tip updateTip(String id, Tip updatedTip) {
		updatedTip.setId(id);
		updatedTip = updateZipCode(updatedTip);
		return tipRep.saveTip(updatedTip);
	}
	
	public void deleteTip(String id) {
		Tip tip = tipRep.findOne(id);
		tip.setIsDeleted(true);
		tipRep.saveTip(tip);
	}
	
	public Tip updateZipCode(Tip tip) {
		// lookup zipcode from the collection ZipCode;
		ZipCode thisZipCode = new ZipCode();
				
		// if the zipCode is not provided by the user
		if (tip.getZipCode()==null) {				
			if (tip.getCityState()==null){
				// if both zipcode and cityState are not completed, set default to 48105
				thisZipCode = zipCodeRep.findByzipCode(48105);
			} else {
				String [] parts = tip.getCityState().split(",");
				String city = parts[0].trim();
				String stateCode = parts[1].trim();	
				thisZipCode = zipCodeRep.findByTownshipLikeIgnoreCaseAndStateCodeLikeIgnoreCase(city, stateCode);				
			}						
		} else {
			/** if the zipCode is provided by the user:
			   (1) ignore stateCity provided by the user, 
			   (2) lookup zipcode from the collection ZipCode
			   (3) please note that the type of zipcode is "int" in the mongoDB collection 
			**/
			thisZipCode = zipCodeRep.findByzipCode(Integer.parseInt(tip.getZipCode()));			
		}
			
		// set longitude and latitude of the family object 
		double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
		tip.setZipCode(thisZipCode.getZipCode());
		tip.setLocation(location);
		tip.setCityState(thisZipCode.getTownship()+", "+thisZipCode.getStateCode());	
		
		return tip;
	}
}
