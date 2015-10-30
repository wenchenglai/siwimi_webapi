package com.adarp.xiwami.test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;
import com.siwimi.webapi.Application;
import com.siwimi.webapi.domain.Group;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.repository.GroupRepository;
import com.siwimi.webapi.repository.MemberRepository;
import com.siwimi.webapi.web.dto.GroupSideload;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GroupControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
    MediaType.APPLICATION_JSON.getSubtype());

	@SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private Member member1,member2,member3,member4,member5;
	
	private Group group1,group2,group3,group4;

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		
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
    	
		/** Setup group **/ 
    	group1 = new Group();
    	group1.setCreator(member1.getId());
    	group1.setTitle("Test Siwimi group 1");
    	group1.setDescription("This is the first test Siwimi group for backend");
    	group1.setMembers(new ArrayList<String>(Arrays.asList(new String[] {member1.getId(),member2.getId(), member4.getId()})));
    	group1.setIsDeletedRecord(false);
    	groupRepository.save(group1);
    	
    	group2 = new Group();
    	group2.setCreator(member3.getId());
    	group2.setTitle("Test Siwimi group 2");
    	group2.setDescription("This is the second test Siwimi group for backend");
    	group2.setMembers(new ArrayList<String>(Arrays.asList(new String[] {member3.getId(),member5.getId(), member1.getId()})));
    	group2.setIsDeletedRecord(false);
    	groupRepository.save(group2);
    	
    	group3 = new Group();
    	group3.setCreator(member5.getId());
    	group3.setTitle("Test Siwimi group 3");
    	group3.setDescription("This is the third test Siwimi group for backend");
    	group3.setMembers(new ArrayList<String>(Arrays.asList(new String[] {member2.getId(),member3.getId(), member5.getId()})));
    	group3.setIsDeletedRecord(false);
    	groupRepository.save(group3);
	}

    /** Test : add, update, and delete group **/
    @Test
    public void testGroup1() throws Exception {  
    	group4 = new Group();
    	group4.setCreator(member1.getId());
    	group4.setTitle("Test Siwimi group 4");
    	group4.setDescription("This is the fourth test Siwimi group for backend");
    	group4.setIsDeletedRecord(false);
    	
    	GroupSideload groupsideLoad4 = new GroupSideload();
    	groupsideLoad4.group = group4;
    	
    	// HTTP.post group4 
    	MvcResult result = mockMvc.perform(post("/groups")
    			          .content(this.json(groupsideLoad4)).accept(contentType).contentType(contentType))
                          .andExpect(status().isOk())
                          .andReturn();
    	
    	// Retrieve group4 id
    	String content = result.getResponse().getContentAsString();    	
    	group4.setId(JsonPath.parse(content).read("$.group.id"));
    	
    	// check group4    	
    	mockMvc.perform(get("/groups/"+ group4.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.group.description", is(group4.getDescription())))
        .andExpect(jsonPath("$.group.title", is(group4.getTitle())));
    	
    	// HTTP.update group4	
    	group4.setTitle("Modified Siwimi test group 4");;
    	mockMvc.perform(put("/groups/"+ group4.getId())
    	.content(this.json(groupsideLoad4)).accept(contentType).contentType(contentType))
    	.andExpect(status().isOk());
    	
    	// check updated group4 
    	mockMvc.perform(get("/groups/"+ group4.getId()))
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.group.description", is(group4.getDescription())))
        .andExpect(jsonPath("$.group.title", is(group4.getTitle())));
    	
    	// HTTP.delete non-existed group
    	mockMvc.perform(delete("/groups/siwimi_noneExistingGroupTest")).andExpect(status().isNotFound());
    	
    	// HTTP.delete group4
    	mockMvc.perform(delete("/groups/"+ group4.getId())).andExpect(status().isNoContent());
    	Group deletedGroup = groupRepository.findOne(group4.getId());
    	assertTrue(deletedGroup.getIsDeletedRecord()); 
    	
    	// delete group4 from database
    	groupRepository.delete(group4);
    }
	
    /** Test :  query group **/
    @Test
    public void testGroup2() throws Exception {   	
    	mockMvc.perform(get("/groups?queryText=second"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.groups[0].title", is(group2.getTitle())));
    	
    	mockMvc.perform(get("/groups?memberId="+member4.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.groups[0].title", is(group1.getTitle())));
    	
    	mockMvc.perform(get("/groups?creator="+member5.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.groups", hasSize(1)))
        .andExpect(jsonPath("$.groups[0].title", is(group3.getTitle())));
    }
    
    /** Remove groups and members from the database, if there is any **/
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
    	if (group1 != null)
    		groupRepository.delete(group1.getId());
    	if (group2 != null)
    		groupRepository.delete(group2.getId());
    	if (group3 != null)
    		groupRepository.delete(group3.getId());
    	if (group4 != null)
    		groupRepository.delete(group4.getId());
    }
    
	@SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
