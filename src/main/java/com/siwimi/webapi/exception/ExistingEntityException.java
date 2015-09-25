package com.siwimi.webapi.exception;

public class ExistingEntityException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String errMsg;
	
	public ExistingEntityException(String errMsg) {
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
