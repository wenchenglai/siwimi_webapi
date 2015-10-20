package com.adarp.xiwami.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import com.siwimi.webapi.Application;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.repository.MemberRepository;
import com.siwimi.webapi.web.dto.MemberSideload;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MemberControllerTest {
	
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());
	
    private MockMvc mockMvc;
    
    @SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	private Member member1,member2,member3,member4,member5 = null;
	
	@Autowired
	private MemberRepository memberRepository;
	
    @Autowired
    private WebApplicationContext webApplicationContext;
    
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
        Member mockMember1 = new Member();
        mockMember1.setFirstName("Siwimi_1");
        mockMember1.setLastName("Lai");
        mockMember1.setFacebookId("1234");
        mockMember1.setZipCode("08540");
        mockMember1.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"Chinese", "English"})));
        this.member1 = memberRepository.save(mockMember1);   
    }
    
    /** Test : find the pre-existing member from the database **/
    @Test
    public void testFindMember1() throws Exception {
    	mockMvc.perform(get("/members/"+ this.member1.getFacebookId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.member.facebookId", is(this.member1.getFacebookId())))
        .andExpect(jsonPath("$.member.lastName", is(this.member1.getLastName())))
        .andExpect(jsonPath("$.member.firstName", is(this.member1.getFirstName())))
        .andExpect(jsonPath("$.member.languages", is(this.member1.getLanguages())))
        .andReturn();
    }
    
    /** Test : add, update, and delete member **/
    @Test
    public void testMember2() throws Exception {
    	
    	// Add new member2
    	this.member2 = new Member();
    	this.member2.setFirstName("Siwimi_2");
    	this.member2.setLastName("Lai_2");
    	this.member2.setFacebookId("Siwimi_1234");
    	this.member2.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"Chinese", "English"})));
    	this.member2.setZipCode("08540");
    	MemberSideload memberSideload = new MemberSideload();
    	memberSideload.member = this.member2;

    	MvcResult result = mockMvc.perform(post("/members")
    			           .content(this.json(memberSideload)).accept(contentType).contentType(contentType))
                           .andExpect(status().isOk())
                           .andReturn();
    	
    	// Retrieve member2 id
    	String content = result.getResponse().getContentAsString();    	
    	this.member2.setId(JsonPath.parse(content).read("$.member.id"));
    	
    	// check member2    	
    	mockMvc.perform(get("/members/"+ this.member2.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.member.facebookId", is(this.member2.getFacebookId())))
        .andExpect(jsonPath("$.member.lastName", is(this.member2.getLastName())))
        .andExpect(jsonPath("$.member.firstName", is(this.member2.getFirstName())))
        .andExpect(jsonPath("$.member.languages", is(this.member2.getLanguages())))
        .andExpect(jsonPath("$.member.city", is("Princeton")))
        .andExpect(jsonPath("$.member.state", is("NJ")));
    	
    	// update member2
    	this.member2.setZipCode("48105");
    	mockMvc.perform(put("/members/"+ this.member2.getId())
    	.content(this.json(memberSideload)).accept(contentType).contentType(contentType))
        .andExpect(status().isOk());

    	// check updated member2 
    	mockMvc.perform(get("/members/"+ this.member2.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.member.facebookId", is(this.member2.getFacebookId())))
        .andExpect(jsonPath("$.member.lastName", is(this.member2.getLastName())))
        .andExpect(jsonPath("$.member.firstName", is(this.member2.getFirstName())))
        .andExpect(jsonPath("$.member.languages", is(this.member2.getLanguages())))
        .andExpect(jsonPath("$.member.city", is("Ann Arbor")))
        .andExpect(jsonPath("$.member.state", is("MI")));
    	
    	// delete non-existed member
    	mockMvc.perform(delete("/members/siwimi_noman")
	    .content(this.json(memberSideload)).accept(contentType).contentType(contentType))
        .andExpect(status().isNotFound());
    	
    	// delete member2
    	mockMvc.perform(delete("/members/"+ this.member2.getId())
	    .content(this.json(memberSideload)).accept(contentType).contentType(contentType))
        .andExpect(status().isNoContent());
    	Member deletedMember = memberRepository.findOne(this.member2.getId());
    	assertTrue(deletedMember.getIsDeletedRecord());
    }
    
    
    /** Test : query member **/
    @Test
    public void testMember345() throws Exception {
    	// create member3 : must set facebookId to avoid sending email
    	this.member3 = new Member();
    	this.member3.setFirstName("a");
    	this.member3.setLastName("Lai_3");
    	this.member3.setFacebookId("Siwimi_3abc");
    	MemberSideload memberSideload3 = new MemberSideload();
    	memberSideload3.member = this.member3;
    	MvcResult result3 = mockMvc.perform(post("/members")
    			           	.content(this.json(memberSideload3)).accept(contentType).contentType(contentType))
    			           	.andExpect(status().isOk())
    			           	.andReturn();   	
    	this.member3.setId(JsonPath.parse(result3.getResponse().getContentAsString()).read("$.member.id"));
    	
    	// create member4 : must set facebookId to avoid sending email  	
    	this.member4 = new Member();
    	this.member4.setFirstName("c");
    	this.member4.setLastName("Lai_3");
    	this.member4.setFacebookId("Siwimi_4abc");
    	MemberSideload memberSideload4 = new MemberSideload();
    	memberSideload4.member = this.member4;
    	MvcResult result4 = mockMvc.perform(post("/members")
		           			.content(this.json(memberSideload4)).accept(contentType).contentType(contentType))
		           			.andExpect(status().isOk())
		           			.andReturn();   	
    	this.member4.setId(JsonPath.parse(result4.getResponse().getContentAsString()).read("$.member.id"));
    	
    	// create member5 : must set facebookId to avoid sending email
    	this.member5 = new Member();
    	this.member5.setFirstName("a");
    	this.member5.setLastName("Shiu_3");
    	this.member5.setFacebookId("Siwimi_5abc");
    	MemberSideload memberSideload5 = new MemberSideload();
    	memberSideload5.member = this.member5;
    	MvcResult result5 = mockMvc.perform(post("/members")
		           			.content(this.json(memberSideload5)).accept(contentType).contentType(contentType))
		           			.andExpect(status().isOk())
		           			.andReturn();   	
    	this.member5.setId(JsonPath.parse(result5.getResponse().getContentAsString()).read("$.member.id"));
    	
    	// query member 
    	mockMvc.perform(get("/members?queryText=Lai_3"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.member", hasSize(2)))
        .andExpect(jsonPath("$.member[0].lastName", is("Lai_3")))
        .andExpect(jsonPath("$.member[0].firstName", is("a")))
        .andExpect(jsonPath("$.member[1].lastName", is("Lai_3")))
        .andExpect(jsonPath("$.member[1].firstName", is("c")));    	
    }
    
    @After
    public void clean() throws Exception {
    	if (this.member1 != null)
    		memberRepository.delete(this.member1.getId());
    	if (this.member2 != null)
    		memberRepository.delete(this.member2.getId());
    	if (this.member3 != null)
    		memberRepository.delete(this.member3.getId());
    	if (this.member4 != null)
    		memberRepository.delete(this.member4.getId());
    	if (this.member5 != null)
    		memberRepository.delete(this.member5.getId());
    }
    
    @SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
