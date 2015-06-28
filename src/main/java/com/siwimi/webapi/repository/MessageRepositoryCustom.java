package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Message;

public interface MessageRepositoryCustom {
	List<Message> query(String fromId, String toId, String fromStatus, String toStatus, String queryText, String sort);
}
