package com.bldj.lexiang.constant.enums;
/** 
 * @author handong E-mail: handong@si-tech.com.cn
 * @version 创建时间：2014-11-5 上午11:50:51 
 * 类说明 
 */
public enum TitleBarEnum {

	ABOUT(0,"关于我们"),	
	FEEDBACK(1,"意见反馈"),	
	SHARE(2,"分享好友"),	
	ZHAOPIN(3,"理疗师招聘"), 
	
	ORDER_SALE(4,"销量排序",2),
	ORDER_TIME(5,"时间排序",0),
	ORDER_PRICE(6,"价格排序",1),
	
	PRICE_NONE(7,"价格区间"),
	PRICE_ORDER1(8,"100-300"),
	PRICE_ORDER2(9,"300-500"),
	
	
	TYPE_ORDER_ALL(10,"全部",2),
	TYPE_ORDER_DOUBLE(11,"双人",1),
	TYPE_ORDER_MANY(12,"多人",1),
	TYPE_ORDER_SINGLE(13,"个人",0),
	
	ORDER_DEFAULT(14,"默认排序"),
	ORDER_DISTANCE(15,"距离排序"),
	ORDER_COUNT(16,"接单次数"),
	
	WORK_3_BIG(17,"三年工作以上"),
	WORK_3_5(18,"工作3-5年"),
	WORK_5_10(19,"工作5-10年"),
	
	SHARE_SINA(20,"新浪微博"),
	SHARE_WEIXIN(21,"微信"),
	SHARE_TENCENT(22,"腾讯微博");
	
	private int index;
	
	private String msg;
	
	private int value;

    private TitleBarEnum(int index,String msg) {
        this.index = index;
        this.msg = msg;
    }
    private TitleBarEnum(int index,String msg,int value) {
        this.index = index;
        this.msg = msg;
        this.value = value;
    }

    public int getValue(){
    	return value;
    }
    
    public int getIndex() {
        return index;
    }
    
    public String getMsg() {
		return msg;
	}

	public static TitleBarEnum getTitleBarResult(int index) {
        for (TitleBarEnum v : TitleBarEnum.values()) {
            if (index == v.getIndex()) {
                return v;
            }
        }
        return ABOUT;
    }
}
