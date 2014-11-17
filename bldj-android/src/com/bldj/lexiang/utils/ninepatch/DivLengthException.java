package com.bldj.lexiang.utils.ninepatch;

/**
 * Created by Anatolii on 8/28/13.
 */
public class DivLengthException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4513206127442614787L;

	public DivLengthException() {
	}

	public DivLengthException(String detailMessage) {
		super(detailMessage);
	}

	public DivLengthException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DivLengthException(Throwable throwable) {
		super(throwable);
	}
}
