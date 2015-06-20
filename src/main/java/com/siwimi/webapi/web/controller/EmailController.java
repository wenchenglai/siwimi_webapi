package com.siwimi.webapi.web.controller;

//@RestController
public class EmailController {
/*	
	@Autowired
	private EmailService emailService;
	
	// add & send New Email
	@RequestMapping(value = "/emails/sent", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Email> sentEmail(@RequestBody EmailSideload newEmail) {
		Email savedEmail = emailService.addEmail(newEmail.email);
		Email sentEmail = emailService.sentEmail(savedEmail);	
		Map<String, Email> responseBody = new HashMap<String, Email>();
		responseBody.put("email", sentEmail);
		
		return responseBody;		
	}	
	
	// Get Email by ID
	@RequestMapping(value = "/emails/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Email> findByEmailId(@PathVariable("id") String id) {
		Map<String,Email> responseBody = new HashMap<String,Email>();			
		Email email = emailService.findByEmailId(id);
		responseBody.put("email", email);
		return responseBody;
	}
		
	// Update Email
	@RequestMapping(value = "/emails/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Email> updateEmail(@PathVariable("id") String id, @RequestBody EmailSideload updatedEmail){
		Email savedEmail = emailService.updateEmail(id, updatedEmail.email);		
		Map<String,Email> responseBody = new HashMap<String, Email>();
		responseBody.put("email", savedEmail);
		return responseBody;			
	}
	
	// Delete Email
	@RequestMapping (value = "/emails/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteEmail(@PathVariable("id")String id) {
		emailService.deleteEmail(id);
	}	*/
}
