package com.gdczwlkj.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class SignatureUtil {

	private static final String signatureParameterName = "sign";

	private static final String secretParameterName = "key";

	public static final String WRONG_SIGN_CODE = "-1";
	public static final String WRONG_SIGN_MSG = "签名错误";

	/**
	 * 生成加上签名后的参数字符串
	 */
	public static String generatePackage(Map<String, String> map, String key){
		String sign = generateSign(map, key);
		Map<String,String> tmap = MapUtil.order(map);
		String parameters = MapUtil.mapJoin(tmap,false,true);
		return parameters + "&" + signatureParameterName + "=" + sign;
	}

	/**
	 * 生成sign AES128 + MD5 toUpperCase
	 */
	public static String generateSign(Map<String, String> map,String secretKey){
		Map<String, String> tmap = MapUtil.order(map);


		String str = MapUtil.mapJoin(tmap, false, false);
		String param = str + "&" + secretParameterName + "=" + secretKey;
		String sign = null;
		try {
			sign = EncrypitonUtil.MD5(param).toUpperCase();
			System.out.println("MD5="+sign);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return sign;
		}
	}

	public static String generateMD5Sign(Map<String, String> map,String secretKey){
		Map<String, String> tmap = MapUtil.order(map);
		String str = MapUtil.mapJoin(tmap, false, false);
		String param = str + "&" + secretParameterName + "=" + secretKey;
		String sign = null;
		try {
			sign = EncrypitonUtil.MD5(param).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return sign;
		}
	}

	/**
	 * 验证签名
	 * @param obj
	 * @return
	 */
	/*public static void validateSign(Object obj, String key) throws RetException{
		try {
			Map<String, String> map = JsonUtil.jsonToStringMap(JsonUtil.toJson(obj));
			String parameterSignValue = map.get("sign");
			if (map.containsKey("serialVersionUID")) {
				map.remove("serialVersionUID");
			}
			if (map.containsKey("total_count")) {
				map.remove("total_count");
			}
			if (map.containsKey("start")) {
				map.remove("start");
			}
			if(map.containsKey("sign")){
				map.remove("sign");
			}
			if(map.containsKey("buyer_remark")){
				map.remove("buyer_remark");
			}
			String sign = generateSign(map, key);
			if (!(sign != null && parameterSignValue != null && sign.equals(parameterSignValue))){
				//throw new ParameterException(WRONG_SIGN_CODE,WRONG_SIGN_MSG);
			}
		} catch (IOException e) {
			e.printStackTrace();

			//throw new ParameterException(WRONG_SIGN_CODE, WRONG_SIGN_MSG);
		}
	}*/

	public static void main(String[] args) {
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", "18312016951");
		map.put("flow_id", "EEEA6D4D-75D8-430F-8E26-01C035137073");
		map.put("login_password", "96E79218965EB72C92A549DD5A330112");
		SignatureUtil signatureUtil = new SignatureUtil();
		String sign = signatureUtil.generateSign(map, "B810A514C67EDC76722E28853DB6D31C");
		System.out.println("sign="+sign);*/
		/*String param = signatureUtil.generatePackage(map);
		System.out.println(param);*/
	}
}
