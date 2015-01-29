package com.bldj.lexiang.api.vo;

import java.io.Serializable;

public class Order implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2250114860985158962L;
	/**
	 * 
	 */
	//
	private String orderNum;//订单号
	private double orderPay;//订单总额
	private String createtime;//下单时间
	private int status;//下单状态
	private String statusStr;//状态
	private long productId;//产品id
	private String proName;//产品名称
	private String sellerName;//卖家名称
	private long sellerId;//卖家id
	private int payType;//支付方式
	private String couponsId;//优惠卷id
	private String servicetime;//预约时间
	
	
	
	public String getCouponsId() {
		return couponsId;
	}
	public void setCouponsId(String couponsId) {
		this.couponsId = couponsId;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public double getOrderPay() {
		return orderPay;
	}
	public void setOrderPay(double orderPay) {
		this.orderPay = orderPay;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getServicetime() {
		return servicetime;
	}
	public void setServicetime(String servicetime) {
		this.servicetime = servicetime;
	}
	
}
