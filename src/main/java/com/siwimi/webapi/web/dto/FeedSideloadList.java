package com.siwimi.webapi.web.dto;

import java.util.List;

import com.siwimi.webapi.domain.Feed;
import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.domain.Member;

public class FeedSideloadList {
		public List<Feed> feeds;
		public List<Member> members; 
		public List<Feedback> feedbacks;
}
