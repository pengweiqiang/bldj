package com.bldj.lexiang.api.bo;
/**
 * 异常实体
 * 
 * @author zhuyb
 * @email zhuyongb0@live.com
 */
public class ExceptionInfo {

	private String curAppVerMsg; //当前app版本号
	
	private String productModel;  //设备硬件信息
	
	private String exceptionMsg;  //错误堆栈信息 

	public ExceptionInfo(String curAppVerMsg,String productModel,String exceptionMsg){
		this.curAppVerMsg = curAppVerMsg;
		this.productModel = productModel;
		this.exceptionMsg = exceptionMsg;
	}
	public ExceptionInfo(){
		
	}
	public String getCurAppVerMsg() {
		return curAppVerMsg;
	}

	public void setCurAppVerMsg(String curAppVerMsg) {
		this.curAppVerMsg = curAppVerMsg;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
}
