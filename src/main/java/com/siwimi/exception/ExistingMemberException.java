package com.siwimi.exception;

public class ExistingMemberException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String errMsg;
	
	public ExistingMemberException(String errMsg) {
		super();
		this.errMsg = errMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
