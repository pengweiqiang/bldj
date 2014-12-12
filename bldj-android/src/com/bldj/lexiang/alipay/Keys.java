/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.bldj.lexiang.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088711154079258";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "15201459685";

	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALUozE53qruvuGka"
			+ "3h0QeNc73ExmYgt2/6KShyAUpIf5caof/gQd6z2iVoJ8uGhp37ULjbAb/bMxU4NF"
			+ "qdnRqvNVtrcBm5Xt9gwoyLBELYqCGjPRVy3MdirAzrrjgTcndlimanaA89EMz4/4"
			+ "ON4cPs65ZYs107ikXuSem9zY+0a1AgMBAAECgYB9JdQOGQkvY3MH2PG8LlbsiYPk"
			+ "m4370ZdD3enPNbN748yr9DHs8iZCi9Uri7FxIELUtUiKcqvOkLLaqtNtkDSBf/8v"
			+ "Jd7N/gN5Wu05izglQOmQH/CYteAqxIb0UlXQAIktuUjzYvwD9VJkg2AS7bx3Uk8X"
			+ "/8oJjUFS+58TENsIQQJBANzwri2wpSnT9j7oug2J2p1INVeRQQP7ZLLq5uZzWGya"
			+ "yK3TIohZ6eUy70suQflCPlVRMIYkLj3dUCj+1gE0Q70CQQDR6BcpW1z85Uf27vGE"
			+ "iXhoAqxO0VrFYEAnGV76p+sQ46WUbJlf0jGDvlzyuUNc25TMCkDuTZBaWBjzCk/u"
			+ "zUJZAkAMJYM96c86vHd2P+8ynViSewzLsqWjZWfE2ls1YjQDTIMp40irvF26hxFW"
			+ "zcU8weKRaDRFu6l7Jzan+lKjoE71AkBPbMCG15lB4Afw+czNV68FvC0yzajJEnhz"
			+ "0vfLZIpfo6Pdm5IVXdV6gidteIhytGuDIUYcZaD/bmJSZE6Gj4hRAkATOZhbeVhW"
			+ "XKJuzmx0yXmoWiC5cpquO8cGoNdsSPg6JUgyEs2KuTUolhrRszcRgu4rFZtn9LmH"
			+ "lUM3ysWx7KEz";

	// 支付宝公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMAXc6KOGd9istqS7cW7GzN4mtjlmUKKxObgJX jYYTVzFzHwDHwHcTT6Sr9yv1uhOs2t1thRywyT7KdZAR40Cvcp+4aFeOHzm1DNneqQnxFxMJ2H05 HhTP59MaY0PcCf8x1WVpuMV+BxGk8CuhPhgWalQtpvS64atqnk1WQIwM1wIDAQAB";

}
