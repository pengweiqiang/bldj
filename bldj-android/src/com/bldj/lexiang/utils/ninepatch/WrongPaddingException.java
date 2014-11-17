package com.bldj.lexiang.utils.ninepatch;

/**
 * Created by Anatolii on 8/28/13.
 */
public class WrongPaddingException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8031525143515105834L;

	public WrongPaddingException() {
	}

	public WrongPaddingException(String detailMessage) {
		super(detailMessage);
	}

	public WrongPaddingException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public WrongPaddingException(Throwable throwable) {
		super(throwable);
	}
}
