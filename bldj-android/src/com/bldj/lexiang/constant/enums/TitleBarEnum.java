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
	
	ZHAOPIN(3,"理疗师招聘");     
	
	private int index;
	
	private String msg;

    private TitleBarEnum(int index,String msg) {
        this.index = index;
        this.msg = msg;
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
