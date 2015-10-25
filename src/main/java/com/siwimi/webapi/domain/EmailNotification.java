package com.siwimi.webapi.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="EmailNotification")
public class EmailNotification {
	
	@Id
	private String id; 
	
	private String creator;	
	private boolean eventNew = false;
	private boolean eventFriendGoing = false;
	private boolean eventFriendConsidering = false;
	private boolean tipNew = false;
	private boolean tipReply = false;
	private boolean questionNew = false;
	private boolean questionReply = false;
	private boolean itemNew = false;
	private boolean itemReply = false;
	private boolean messageReply = false;
	private boolean groupAddMember = false;
		
	//The default of the below field is set by back end
	private boolean isDeletedRecord = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public boolean getEventNew() {
		return eventNew;
	}

	public void setEventNew(boolean eventNew) {
		this.eventNew = eventNew;
	}

	public boolean getEventFriendGoing() {
		return eventFriendGoing;
	}

	public void setEventFriendGoing(boolean eventFriendGoing) {
		this.eventFriendGoing = eventFriendGoing;
	}

	public boolean getEventFriendConsidering() {
		return eventFriendConsidering;
	}

	public void setEventFriendConsidering(boolean eventFriendConsidering) {
		this.eventFriendConsidering = eventFriendConsidering;
	}

	public boolean getTipNew() {
		return tipNew;
	}

	public void setTipNew(boolean tipNew) {
		this.tipNew = tipNew;
	}

	public boolean getTipReply() {
		return tipReply;
	}

	public void setTipReply(boolean tipReply) {
		this.tipReply = tipReply;
	}

	public boolean getQuestionNew() {
		return questionNew;
	}

	public void setQuestionNew(boolean questionNew) {
		this.questionNew = questionNew;
	}

	public boolean getQuestionReply() {
		return questionReply;
	}

	public void setQuestionReply(boolean questionReply) {
		this.questionReply = questionReply;
	}

	public boolean getItemNew() {
		return itemNew;
	}

	public void setItemNew(boolean itemNew) {
		this.itemNew = itemNew;
	}

	public boolean getItemReply() {
		return itemReply;
	}

	public void setItemReply(boolean itemReply) {
		this.itemReply = itemReply;
	}

	public boolean getMessageReply() {
		return messageReply;
	}

	public void setMessageReply(boolean messageReply) {
		this.messageReply = messageReply;
	}

	public boolean getGroupAddMember() {
		return groupAddMember;
	}

	public void setGroupAddMember(boolean groupAddMember) {
		this.groupAddMember = groupAddMember;
	}

	public boolean getIsDeletedRecord() {
		return isDeletedRecord;
	}

	public void setIsDeletedRecord(boolean isDeletedRecord) {
		this.isDeletedRecord = isDeletedRecord;
	}	
}
