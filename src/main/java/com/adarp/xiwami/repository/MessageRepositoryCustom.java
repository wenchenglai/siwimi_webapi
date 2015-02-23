package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Message;

public interface MessageRepositoryCustom {
	List<Message> query(String fromId, String toId, String fromStatus, String toStatus, String queryText);
	//Message save(Message newObj);
}
