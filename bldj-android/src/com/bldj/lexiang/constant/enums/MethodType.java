package com.bldj.lexiang.constant.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 方法枚举
 * @author handong
 * @email dong.han1@renren-inc.com
 */
public enum MethodType {

	GET_MAINPAGE_AD(0),   //获取广告topOne
	
    SUBMIT_ORDER(51);  //提交订单
	
	private int index;
	
	private static Map<Integer, MethodType> map;
	
	static {
        map = new HashMap<Integer, MethodType>();
        for (MethodType ust : MethodType.values()) {
            map.put(ust.getIndex(), ust);
        }
    }
	private MethodType(int index) {
        this.index = index;
    }
	public int getIndex() {
		return index;
	}
	public static Map<Integer, MethodType> getMap() {
        return map;
    }
}
