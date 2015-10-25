package com.adarp.xiwami.test;

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
import java.util.List;

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
import com.siwimi.webapi.domain.Family;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.repository.FamilyRepository;
import com.siwimi.webapi.repository.MemberRepository;
import com.siwimi.webapi.web.dto.FamilySideload;
import com.siwimi.webapi.web.dto.MemberSideload;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FamilyControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());
    
    @SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;
    
	private MemberSideload memberSideload1,memberSideload2,memberSideload3,memberSideload4,memberSideload5 = new MemberSideload();
	
	private FamilySideload familySideload1,familySideload2,familySideload3,familySideload4 = new FamilySideload() ;
  
	@Autowired
	private FamilyRepository familyRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
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
        
        /** Setup family **/
    	Family family = new Family();  	
    	family.setIsDeletedRecord(false);
    	
/*    	family.setZipCode("08540");
    	family.setDescription("AAA");
    	family.setFamilyName("Family A");  	
    	this.familySideload1.family = familyRepository.saveFamily(family);
    	
    	family.setZipCode("48105");
    	family.setDescription("BBB");
    	family.setFamilyName("Family B");  	
    	this.familySideload2.family = familyRepository.saveFamily(family);
    	
    	family.setZipCode("10606");
    	family.setDescription("CCC");
    	family.setFamilyName("Family C");  	
    	this.familySideload3.family = familyRepository.saveFamily(family);*/
 
    	family.setZipCode("10606");
    	family.setDescription("DDD");
    	family.setFamilyName("Family D");  
    	this.familySideload4.family = family;
    	
        /** Setup member **/
/*    	Member member = new Member();
    	
    	member.setFirstName("Siwimi_FirstName_1");
    	member.setLastName("Siwimi_LastName_1");
    	member.setFacebookId("Siwimi_FB_1");
    	member.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"Chinese", "English"})));
    	member.setZipCode("08540");
    	member.setFamily(this.familySideload1.family.getId());
    	this.memberSideload1 = new MemberSideload();
    	this.memberSideload1.member = memberRepository.save(member);
    	
    	member.setFirstName("Siwimi_FirstName_2");
    	member.setLastName("Siwimi_LastName_2");
    	member.setFacebookId("Siwimi_FB_2");
    	member.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"Chinese", "Korean"})));
    	member.setZipCode("08540");
    	member.setFamily(this.familySideload1.family.getId());
    	this.memberSideload2 = new MemberSideload();
    	this.memberSideload2.member = memberRepository.save(member);
    	
    	member.setFirstName("Siwimi_FirstName_3");
    	member.setLastName("Siwimi_LastName_3");
    	member.setFacebookId("Siwimi_FB_3");
    	member.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"Spanish", "English"})));
    	member.setZipCode("48105");
    	member.setFamily(this.familySideload2.family.getId());
    	this.memberSideload3 = new MemberSideload();
    	this.memberSideload3.member = memberRepository.save(member);

    	member.setFirstName("Siwimi_FirstName_4");
    	member.setLastName("Siwimi_LastName_4");
    	member.setFacebookId("Siwimi_FB_4");
    	member.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"Chinese"})));
    	member.setZipCode("48105");
    	member.setFamily(this.familySideload2.family.getId());
    	this.memberSideload4 = new MemberSideload();
    	this.memberSideload4.member = memberRepository.save(member);
    	
    	member.setFirstName("Siwimi_FirstName_5");
    	member.setLastName("Siwimi_LastName_5");
    	member.setFacebookId("Siwimi_FB_5");
    	member.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"English"})));
    	member.setZipCode("10606");
    	member.setFamily(this.familySideload3.family.getId());
    	this.memberSideload5 = new MemberSideload();
    	this.memberSideload5.member = memberRepository.save(member);*/
     	
    }
    
    /** Test : add, update, and delete member **/
    @Test
    public void testFamily1() throws Exception {   	
    	// Add new family4 
    	MvcResult result = mockMvc.perform(post("/families")
    			          .content(this.json(familySideload4)).accept(contentType).contentType(contentType))
                          .andExpect(status().isOk())
                          .andReturn();
    	// Retrieve family4 id
    	String content = result.getResponse().getContentAsString();    	
    	this.familySideload4.family.setId(JsonPath.parse(content).read("$.family.id"));
    	
    	// check family4    	
    	mockMvc.perform(get("/families/"+ this.familySideload4.family.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.family.description", is(this.familySideload4.family.getDescription())))
        .andExpect(jsonPath("$.family.familyName", is(this.familySideload4.family.getFamilyName())))
        .andExpect(jsonPath("$.family.city", is("White Plains")))
        .andExpect(jsonPath("$.family.state", is("NY")));
    	
    	// update family4	
    	this.familySideload4.family.setZipCode("27705");
    	mockMvc.perform(put("/families/"+ this.familySideload4.family.getId())
    	.content(this.json(familySideload4)).accept(contentType).contentType(contentType))
    	.andExpect(status().isOk());
    	
    	// check updated family4 
    	mockMvc.perform(get("/families/"+ this.familySideload4.family.getId()))
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(contentType))
    	.andExpect(jsonPath("$.family.description", is(this.familySideload4.family.getDescription())))
    	.andExpect(jsonPath("$.family.familyName", is(this.familySideload4.family.getFamilyName())))
    	.andExpect(jsonPath("$.family.city", is("Durham")))
        .andExpect(jsonPath("$.family.state", is("NC")));
    	
    	// delete non-existed family
    	mockMvc.perform(delete("/families/siwimi_noFamily")).andExpect(status().isNotFound());
    	
    	// delete family4
    	mockMvc.perform(delete("/families/"+ this.familySideload4.family.getId())).andExpect(status().isOk());
    	Family deletedFamily = familyRepository.findOne(this.familySideload4.family.getId());
    	assertTrue(deletedFamily.getIsDeletedRecord());
    	
    }
    
    @After
    public void clean() throws Exception {
/*    	if (this.memberSideload1.member != null)
    		memberRepository.delete(this.memberSideload1.member.getId());
    	if (this.memberSideload2.member != null)
    		memberRepository.delete(this.memberSideload2.member.getId());
    	if (this.memberSideload3.member != null)
    		memberRepository.delete(this.memberSideload3.member.getId());
    	if (this.memberSideload4.member != null)
    		memberRepository.delete(this.memberSideload4.member.getId());
    	if (this.memberSideload5.member != null)
    		memberRepository.delete(this.memberSideload5.member.getId());
    	if (this.familySideload1.family != null)
    		familyRepository.delete(this.familySideload1.family.getId());
    	if (this.familySideload2.family != null)
    		familyRepository.delete(this.familySideload2.family.getId());
    	if (this.familySideload3.family != null)
    		familyRepository.delete(this.familySideload3.family.getId());*/
    	if (this.familySideload4.family != null)
    		familyRepository.delete(this.familySideload4.family.getId());
    }
    
    @SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
