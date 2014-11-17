package com.bldj.lexiang.api.vo;

public class ParseModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object apiResult;
	
	private int resultCount; //总数

	public Object getApiResult() {
		return apiResult;
	}

	public void setApiResult(Object apiResult) {
		this.apiResult = apiResult;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
}
