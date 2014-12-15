package com.bldj.lexiang.constant.enums;

public enum OrderStatusEnum {
	NO_PAID(0, "未支付"), PAID_ONLINE(1, "线上已支付"), PAID_OFFLINE(2, "线下已支付"), CANCLED(
			3, "已取消"), SELLER_CONFIRM(4, "卖家已发货"), BUYER_CONFIRM(5, "买家已收货"), COMPLETE(
			6, "交易成功");

	private int status;
	private String statusStr;

	private OrderStatusEnum(int status, String statusStr) {
		this.status = status;
		this.statusStr = statusStr;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
	
}
