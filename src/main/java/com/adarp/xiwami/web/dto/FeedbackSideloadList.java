package com.adarp.xiwami.web.dto;

import java.util.List;

import com.adarp.xiwami.domain.Feedback;
import com.adarp.xiwami.domain.Member;

public class FeedbackSideloadList {
	public List<Feedback> feedbacks;
	public List<Feedback> comments;
	public List<Member> feedbacksMemebers;
	public List<Member> CommentsMembers;
}
