package com.adarp.xiwami.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.adarp.xiwami.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MemberControllerTest {
	
	private RestTemplate restTemplate = new RestTemplate(); 
    
    @Test
    public void testFindFamilies() {

    	/** facebookId=1234 must not exist in the database **/
    	//String url = "http://localhost:8088/members?facebookId=1234";
    	//assertNull(restTemplate.getForObject(url, Map.class).get("member"));
    }

}
