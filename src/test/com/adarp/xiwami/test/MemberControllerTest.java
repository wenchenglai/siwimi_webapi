package com.adarp.xiwami.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        mockMember1.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"Chinese", "English"})));
        this.member1 = memberRepository.save(mockMember1);   
    }
    
    // find pre-existing member1 from the database
    @Test
    public void testFindMember1() throws Exception {
    	mockMvc.perform(get("/members/"+ this.member1.getFacebookId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.member.facebookId", is(this.member1.getFacebookId())))
                .andExpect(jsonPath("$.member.lastName", is(this.member1.getLastName())))
                .andExpect(jsonPath("$.member.firstName", is(this.member1.getFirstName())))
                .andExpect(jsonPath("$.member.languages", is(this.member1.getLanguages())));
    }
    
    // add member2 into the database
    @Test
    public void testAddMember2() throws Exception {
    	this.member2 = new Member();
    	this.member2.setFirstName("Siwimi_2");
    	this.member2.setLastName("Lai_2");
    	this.member2.setFacebookId("Siwimi_1234");
    	this.member2.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"Chinese", "English"})));
    	this.member2.setZipCode("08540");
    	MemberSideload memberSideload = new MemberSideload();
    	memberSideload.member = this.member2;

    	mockMvc.perform(post("/members").content(this.json(memberSideload)).accept(contentType).contentType(contentType))
                .andExpect(status().isOk());
    	
    	mockMvc.perform(get("/members/"+ this.member2.getFacebookId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.member.facebookId", is(this.member2.getFacebookId())))
        .andExpect(jsonPath("$.member.lastName", is(this.member2.getLastName())))
        .andExpect(jsonPath("$.member.firstName", is(this.member2.getFirstName())))
        .andExpect(jsonPath("$.member.languages", is(this.member2.getLanguages())));
    }
    
    @After
    public void clean() throws Exception {
    	memberRepository.delete(this.member1.getId());
    	if (this.member2 != null)
    		memberRepository.delete(memberRepository.queryExistingMember(this.member2.getFacebookId()).getId());
    }
    
    @SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
