package com.siwimi.webapi.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;

import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.domain.Location;
import com.siwimi.webapi.repository.ActivityRepository;
import com.siwimi.webapi.repository.LocationRepository;

@Service
public class ParseEventService {
	
	@Autowired
	private ActivityRepository activityRep;
	
	@Autowired
	private LocationRepository locationRep;
	
	/** Get events from aadl.org **/
	public List<Activity> parseAADL() {
		
		List<Activity> activities = new ArrayList<Activity>();

		// Retrieve five pages from AADL
		for (int i=0; i<5; i++) {
			String activitiesUrl = i==0 ? "http://www.aadl.org/events#upcoming": "http://www.aadl.org/events?page="+i;
			Document doc = null; 
			try {
				// Build up connetion, and parse activities
				doc = Jsoup.connect(activitiesUrl).get();
				String parseActivity = "node node-type-pub-event node-teaser build-mode-teaser with-picture";
				Elements divs = doc.getElementsByAttributeValue("class", parseActivity);
				
				// Retrieve data from a single activity
				for (Element e : divs) {
					String eventUrl = e.select("h2.title a").attr("href");
					String title = e.select("h2.title a").text();
					String content1 = null;
					String content2 = null;
					for (Element content : e.getElementsByClass("content")) {
						content1 = content.select("h3").text();
						content2 = content.select("p").text();
					}
					// Query database to ensure no duplicated activity					
					List<Activity> previous = new ArrayList<Activity>();
					String[] parts = eventUrl.split("/");
					String nodeId = parts[2];
					if (nodeId!=null)
						previous = activityRep.queryActivity("Ann Arbor Distric Library : "+nodeId, null, null, null, null, null, 
							                                  null, null, null, null, null, null, null);
					// Populate data into activity object	
					if ((title!=null) && !title.isEmpty() && previous.isEmpty() && (nodeId!=null)) {
						Activity activity = new Activity();
						// Populate activity : creator Id. We need this ensure no duplicated activities posted by robot
						activity.setCreator("Ann Arbor Distric Library : "+nodeId);
						// Populate activity : url
						if (eventUrl!=null)
							activity.setUrl("http://www.aadl.org"+eventUrl);
						// Populate activity : title
						activity.setTitle(title);
						// Populate created date
						activity.setCreatedDate(new Date());
						// Populate activity : imageUrl and imageData
						String imageUrl = e.select("img").attr("src");
						if (imageUrl!=null) {
							activity.setImageUrl(imageUrl);		
							// Populate activity : imageData
							Response resultImageResponse = Jsoup.connect(imageUrl).ignoreContentType(true).execute();
							BASE64Encoder encoder = new BASE64Encoder();
							String imageData = encoder.encode(resultImageResponse.bodyAsBytes());
							//System.out.println(imageData);
							if (imageUrl.contains(".jpg")) {
								imageData = "data:image/jpeg;base64," + imageData;
							} else if (imageUrl.contains(".png")) {
								imageData = "data:image/png;base64," + imageData;	
							}
							activity.setImageData(imageData);
						}					
						// Populate activity : description
						activity.setDescription(content2);
						if (content1 != null) {
							String [] part1;
							String [] part2;
							String [] part3;
							if (content1.contains("through")) {
								part1 = content1.split("through");
								SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm aaa");
								//populate fromDate
								if (part1[0].toLowerCase().contains("now")) {
									activity.setFromDate(new Date());
								} else {
									try {
										Date fromDate = formatter.parse(part1[0].trim()+" 8:00 am");
										activity.setFromDate(fromDate);
									} catch (ParseException e1) {
										e1.printStackTrace();
									}
								}
								//populate toDate
								part2 = part1[1].split("--");
								try {
									Date toDate = formatter.parse(part2[0].trim()+" 8:00 pm");
									activity.setToDate(toDate);
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
								part3 = part2;		
							} else {
								part1 = content1.split(":");
								//populate fromTime
								content1 = content1.replaceAll(part1[0]+":", "");
								part2 = content1.trim().split("to");
								activity.setFromTime(part2[0].trim());
								//populate toTime
								content1 = content1.replaceAll(part2[0]+"to", "");
								part3 = content1.trim().split("--");		
								activity.setToTime(part3[0].trim());
								// Populate activity : fromDate and toDate
								SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMMM dd, yyyy hh:mm aaa");
								try {
									Date fromDate = formatter.parse(part1[0].trim()+" "+part2[0].trim());
									activity.setFromDate(fromDate);
									Date toDate = formatter.parse(part1[0].trim()+" "+part3[0].trim());
									activity.setToDate(toDate);
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
							}
								
							// Populate activity : address and zipCode
							if (part3[1].contains("Malletts Creek")) {
								activity.setAddress("3090 E. Eisenhower Parkway (The Ann Arbor District Library, "
							                        + part3[1].trim()
							                        + ")");
								activity.setZipCode("48108");
							} else if (part3[1].contains("Traverwood")) {
								activity.setAddress("3333 Traverwood Drive (The Ann Arbor District Library, "
				                        + part3[1].trim()
				                        + ")");
								activity.setZipCode("48105");
							}  else if (part3[1].contains("West Branch")) {
								activity.setAddress("2503 Jackson Ave (The Ann Arbor District Library, "
				                        + part3[1].trim()
				                        + ")");
								activity.setZipCode("48103");
							} else if (part3[1].contains("Pittsfield")) {
								activity.setAddress("2359 Oak Valley Dr. (The Ann Arbor District Library, "
				                        + part3[1].trim()
				                        + ")");
								activity.setZipCode("48103");
							} else if (part3[1].contains("Downtown")) {
								activity.setAddress("343 South Fifth Avenue (The Ann Arbor District Library, "
				                        + part3[1].trim()
				                        + ")");
								activity.setZipCode("48104");
							} else {
								activity.setZipCode("48105");
							}
						}												
						activity.setIsDeletedRecord(false);
						activity.setViewCount(0);
						activity = updateLocationAndTime(activity);
						activities.add(activityRep.saveActivity(activity));
					}										
				}				
			} catch (IOException e) {				
			}						
		}
		return activities;
	}

	public Activity updateLocationAndTime(Activity activity) {
		// lookup location from the collection Location;
		Location thisLocation = locationRep.queryLocation(activity.getZipCode(), activity.getCity(), activity.getState());
		// set longitude and latitude 
		if (thisLocation!=null) {
			double[] location = {thisLocation.getLongitude(), thisLocation.getLatitude()};
			activity.setZipCode(thisLocation.getZipCode());
			activity.setLocation(location);
			activity.setCity(thisLocation.getTownship());
			activity.setState(thisLocation.getStateCode());
		}

		SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMMM dd, yyyy hh:mm aaa");
	    String fromDateString = formatter.format(activity.getFromDate());	 
	    String toDateString = formatter.format(activity.getToDate());
	    if (thisLocation.getTimezone() != null) {
	    	if (thisLocation.getTimezone().contains("-5"))
	    		formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
	    	else if (thisLocation.getTimezone().contains("-6"))
	    		formatter.setTimeZone(TimeZone.getTimeZone("America/Winnipeg"));
	    	else if (thisLocation.getTimezone().contains("-7"))
	    		formatter.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
	    	else if (thisLocation.getTimezone().contains("-8"))
	    		formatter.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));		    	
	    } else {
	    	formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
	    }	    
	   try {
		   activity.setFromDate(formatter.parse(fromDateString));
		   activity.setToDate(formatter.parse(toDateString));
	   } catch (ParseException e) {
		   e.printStackTrace();
	   }								
		return activity;
	}	
}
