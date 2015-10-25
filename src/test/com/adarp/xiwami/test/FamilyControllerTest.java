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
import com.siwimi.webapi.domain.Family;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.repository.FamilyRepository;
import com.siwimi.webapi.repository.MemberRepository;
import com.siwimi.webapi.service.FamilyService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.FamilySideload;

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
    
	private Member member1,member2,member3,member4,member5;
	
	private Family family1,family2,family3,family4;
  
	@Autowired
	private FamilyRepository familyRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private FamilyService familyService;
	
	@Autowired
	private MemberService memberService;
	
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
     	family1 = new Family();
     	family1.setZipCode("08540");
     	family1.setDescription("AAA");
     	family1.setFamilyName("Family A"); 
     	family1.setIsDeletedRecord(false);
    	family1 = familyService.addFamily(family1);
    	
    	family2 = new Family();
    	family2.setZipCode("48105");
    	family2.setDescription("BBB");
    	family2.setFamilyName("Family B");  
    	family2.setIsDeletedRecord(false);
    	family2 = familyService.addFamily(family2);
    	
    	family3 = new Family();
    	family3.setZipCode("10606");
    	family3.setDescription("CCC");
    	family3.setFamilyName("Family C"); 
    	family3.setIsDeletedRecord(false);
    	family3 = familyService.addFamily(family3);
    	
        /** Setup member **/    
    	member1 = new Member();
    	member1.setFirstName("Siwimi_FirstName_1");
    	member1.setLastName("Siwimi_LastName_1");
    	member1.setFacebookId("Siwimi_FB_1");
    	member1.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"T_Chinese", "T_English"})));
    	member1.setZipCode("08540");
    	member1.setFamily(this.family1.getId());
        Calendar cal1 = Calendar.getInstance();   
        cal1.add(Calendar.YEAR, -35); // 35 years old
    	member1.setBirthday(cal1.getTime());
    	member1.setIsDeletedRecord(false);
    	member1 = memberService.addMember(member1);
    	
    	member2 = new Member();
    	member2.setFirstName("Siwimi_FirstName_2");
    	member2.setLastName("Siwimi_LastName_2");
    	member2.setFacebookId("Siwimi_FB_2");
    	member2.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"T_Chinese", "T_Korean"})));
    	member2.setZipCode("08540");
    	member2.setFamily(this.family1.getId());
        Calendar cal2 = Calendar.getInstance();   
        cal2.add(Calendar.YEAR, -5); // 5 years old
    	member2.setBirthday(cal2.getTime());
    	member2.setIsDeletedRecord(false);
    	member2 = memberService.addMember(member2);
    	
    	member3 = new Member();
    	member3.setFirstName("Siwimi_FirstName_3");
    	member3.setLastName("Siwimi_LastName_3");
    	member3.setFacebookId("Siwimi_FB_3");
    	member3.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"T_Spanish", "T_English"})));
    	member3.setZipCode("48105");
    	member3.setFamily(this.family2.getId());
        Calendar cal3 = Calendar.getInstance();   
        cal3.add(Calendar.YEAR, -30); // 30 years old
    	member3.setBirthday(cal3.getTime());
    	member3.setIsDeletedRecord(false);
    	member3 = memberService.addMember(member3);

    	member4 = new Member();
    	member4.setFirstName("Siwimi_FirstName_4");
    	member4.setLastName("Siwimi_LastName_4");
    	member4.setFacebookId("Siwimi_FB_4");
    	member4.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"T_Chinese"})));
    	member4.setZipCode("48105");
    	member4.setFamily(this.family2.getId());
        Calendar cal4 = Calendar.getInstance();   
        cal4.add(Calendar.YEAR, -2); // 2 years old
    	member4.setBirthday(cal4.getTime());
    	member4.setIsDeletedRecord(false);
    	member4 = memberService.addMember(member4);
    	
    	member5 = new Member();
    	member5.setFirstName("Siwimi_FirstName_5");
    	member5.setLastName("Siwimi_LastName_5");
    	member5.setFacebookId("Siwimi_FB_5");
    	member5.setLanguages(new ArrayList<String>(Arrays.asList(new String[] {"T_English"})));
    	member5.setZipCode("10606");
    	member5.setFamily(this.family3.getId());
    	member5.setIsDeletedRecord(false);
    	member5 = memberService.addMember(member5);    	
    }
    
    /** Test : add, update, and delete family **/
    @Test
    public void testFamily1() throws Exception {   	
    	family4 = new Family();
    	family4.setZipCode("10606");
    	family4.setDescription("DDD");
    	family4.setFamilyName("Family D");
    	family4.setIsDeletedRecord(false);
    	FamilySideload familySideload4 = new FamilySideload();
    	familySideload4.family = family4;
    	
    	// HTTP.post family4 
    	MvcResult result = mockMvc.perform(post("/families")
    			          .content(this.json(familySideload4)).accept(contentType).contentType(contentType))
                          .andExpect(status().isOk())
                          .andReturn();
    	// Retrieve family4 id
    	String content = result.getResponse().getContentAsString();    	
    	family4.setId(JsonPath.parse(content).read("$.family.id"));
    	
    	// check family4    	
    	mockMvc.perform(get("/families/"+ family4.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.family.description", is(family4.getDescription())))
        .andExpect(jsonPath("$.family.familyName", is(family4.getFamilyName())))
        .andExpect(jsonPath("$.family.city", is("White Plains")))
        .andExpect(jsonPath("$.family.state", is("NY")));
    	
    	// HTTP.update family4	
    	family4.setZipCode("27705");
    	mockMvc.perform(put("/families/"+ family4.getId())
    	.content(this.json(familySideload4)).accept(contentType).contentType(contentType))
    	.andExpect(status().isOk());
    	
    	// check updated family4 
    	mockMvc.perform(get("/families/"+ family4.getId()))
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(contentType))
    	.andExpect(jsonPath("$.family.description", is(family4.getDescription())))
    	.andExpect(jsonPath("$.family.familyName", is(family4.getFamilyName())))
    	.andExpect(jsonPath("$.family.city", is("Durham")))
        .andExpect(jsonPath("$.family.state", is("NC")));
    	
    	// HTTP.delete non-existed family
    	mockMvc.perform(delete("/families/siwimi_noFamily")).andExpect(status().isNotFound());
    	
    	// HTTP.delete family4
    	mockMvc.perform(delete("/families/"+ family4.getId())).andExpect(status().isOk());
    	Family deletedFamily = familyRepository.findOne(family4.getId());
    	assertTrue(deletedFamily.getIsDeletedRecord());    	
    	
    	// delete family4 from database
    	familyRepository.delete(family4.getId());
    }
    
    /** Test :  query family **/
    @Test
    public void testFamily2() throws Exception {   	
    	mockMvc.perform(get("/families?latitude=40.49598&longitude=-74.4377&distance=150 mile&languages[]=T_English"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.family", hasSize(2)));
  
    	mockMvc.perform(get("/families?latitude=40.49598&longitude=-74.4377&distance=150 mile&fromAge=3&toAge=6&languages[]=T_Chinese"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.family", hasSize(1)))
        .andExpect(jsonPath("$.family[0].description", is("AAA")));
  
    	mockMvc.perform(get("/families?latitude=40.4959&longitude=-74.4377&distance=950 mile&fromAge=3&toAge=6&languages[]=T_Korean&languages[]=T_Spanish"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.family", hasSize(1)))
        .andExpect(jsonPath("$.family[0].description", is("AAA")));
  
    	mockMvc.perform(get("/families?latitude=40.49598&longitude=-74.4377&distance=950 mile&languages[]=T_Korean&languages[]=T_Spanish"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.family", hasSize(2)));

    	// delete families and members from database
    	familyRepository.delete(family1.getId());
    	familyRepository.delete(family2.getId());
    	familyRepository.delete(family3.getId());
    	memberRepository.delete(member1.getId());
    	memberRepository.delete(member2.getId());
    	memberRepository.delete(member3.getId());
    	memberRepository.delete(member4.getId());
    	memberRepository.delete(member5.getId());
    }
    
    /** Remove family and members from the database, if there is any **/
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
    	if (family1 != null)
    		familyRepository.delete(family1.getId());
    	if (family2 != null)
    		familyRepository.delete(family2.getId());
    	if (family3 != null)
    		familyRepository.delete(family3.getId());
    	if (family4 != null)
    		familyRepository.delete(family4.getId());
    }
    
    @SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
