package com.adarp.xiwami.test;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.siwimi.webapi.Application;
import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.repository.ActivityRepository;
import com.siwimi.webapi.repository.MemberRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ActivityControllerTest {
//	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),MediaType.APPLICATION_JSON.getSubtype());

	@SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

//	private MockMvc mockMvc;

	private Member member1,member2,member3,member4,member5;
	
	private Activity activity1,activity2,activity3,activity4;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ActivityRepository activityRepository;
	
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
//		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		
	       /** Setup member **/    
	    	member1 = new Member();
	    	member1.setFirstName("Siwimi_FirstName_1");
	    	member1.setLastName("Siwimi_LastName_1");
	    	member1.setIsDeletedRecord(false);
	    	member1 = memberRepository.save(member1);
	    	
	    	member2 = new Member();
	    	member2.setFirstName("Siwimi_FirstName_2");
	    	member2.setLastName("Siwimi_LastName_2");
	    	member2.setIsDeletedRecord(false);
	    	member2 = memberRepository.save(member2);
	    	
	    	member3 = new Member();
	    	member3.setFirstName("Siwimi_FirstName_3");
	    	member3.setLastName("Siwimi_LastName_3");
	    	member3.setIsDeletedRecord(false);
	    	member3 = memberRepository.save(member3);

	    	member4 = new Member();
	    	member4.setFirstName("Siwimi_FirstName_4");
	    	member4.setLastName("Siwimi_LastName_4");
	    	member4.setIsDeletedRecord(false);
	    	member4 = memberRepository.save(member4);
	    	
	    	member5 = new Member();
	    	member5.setFirstName("Siwimi_FirstName_5");
	    	member5.setLastName("Siwimi_LastName_5");
	    	member5.setIsDeletedRecord(false);
	    	member5 = memberRepository.save(member5);
	    	
	        /** Setup activity **/
	    	activity1 = new Activity();
	    	activity1.setCreator(member1.getId());
	    	activity1.setTitle("Siwimi event #1");
	    	activity1.setDescription("This is the first event!");
	}

    /** Remove activities and members from the database, if there is any **/
    @After
    public void clean() throws Exception {
    	if (member1 != null)
    		memberRepository.delete(member1.getId());
    	if (member2 != null)
    		memberRepository.delete(member2.getId());
    	if (member3 != null)
    		memberRepository.delete(member3.getId());
    	if (member4 != null)
    		memberRepository.delete(member4.getId());
    	if (member5 != null)
    		memberRepository.delete(member5.getId());
    	if (activity1 != null)
    		activityRepository.delete(activity1.getId());
    	if (activity2 != null)
    		activityRepository.delete(activity2.getId());
    	if (activity3 != null)
    		activityRepository.delete(activity3.getId());
    	if (activity4 != null)
    		activityRepository.delete(activity4.getId());
    }
	
	
	@SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
