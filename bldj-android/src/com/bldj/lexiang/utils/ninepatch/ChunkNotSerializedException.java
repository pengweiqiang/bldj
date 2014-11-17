package com.bldj.lexiang.utils.ninepatch;

/**
 * Created by Anatolii on 8/28/13.
 */
public class ChunkNotSerializedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6423024965623760865L;

	public ChunkNotSerializedException() {
	}

	public ChunkNotSerializedException(String detailMessage) {
		super(detailMessage);
	}

	public ChunkNotSerializedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ChunkNotSerializedException(Throwable throwable) {
		super(throwable);
	}
}
