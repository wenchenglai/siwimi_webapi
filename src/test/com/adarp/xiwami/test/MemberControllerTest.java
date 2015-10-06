package com.adarp.xiwami.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.siwimi.webapi.Application;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.repository.MemberRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
@WebAppConfiguration
//@IntegrationTest("server.port:0")
//@ContextConfiguration({ "classpath:springDispatcher-servlet.xml" })
//@WebIntegrationTest({"server.port=8080", "management.port=8080"})
public class MemberControllerTest {
	
	//@Value("${local.server.port}")
	//private int port;
	
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());
	
    private MockMvc mockMvc;
    
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	private Member member;
	
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
    	//MockitoAnnotations.initMocks(this);
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    	//this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.memberRepository.deleteAll();
        Member mockMember = new Member();
        mockMember.setFacebookId("1234");
        this.member = memberRepository.save(mockMember);        
    }
    
    @Test
    public void testFindMembers() throws Exception {
    	mockMvc.perform(get("/members/"+ this.member.getFacebookId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.member.facebookId", is(this.member.getFacebookId())));

/*        this.mockMvc.perform(get("/member"+this.member.getFacebookId()).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.facebookId").value("this.member.getFacebookId()"));*/
    }
	//private RestTemplate restTemplate = new RestTemplate(); 
    
    //@Test
 //   public void testFindFamilies() {

    	/** facebookId=1234 must not exist in the database **/
    	//assertNull(null);
    	//String url = "http://localhost:8088/members?facebookId=1234";
    	//assertNull(restTemplate.getForObject(url, Map.class).get("member"));
   // }

}
