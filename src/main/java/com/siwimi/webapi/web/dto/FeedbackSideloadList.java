package com.siwimi.webapi.web.dto;

import java.util.List;

import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.domain.Member;

public class FeedbackSideloadList {
	public List<Feedback> feedbacks;
	public List<Feedback> comments;
	public List<Member> members;
}
