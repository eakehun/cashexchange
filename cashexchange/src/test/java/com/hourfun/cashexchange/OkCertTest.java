package com.hourfun.cashexchange;

import com.google.gson.JsonObject;

import kcb.module.v3.OkCert;
import kcb.module.v3.exception.OkCertException;
import kcb.org.json.JSONObject;

public class OkCertTest {
	public static void main(String[] args) {
		
	
		String target = "PROD";
		String cpCd = "V47630000000";
		String svcName = "IDS_HS_POPUP_START";
		
		String license = "C:/Users/gnogu/Downloads/V47630000000_IDS_01_PROD_AES_license.dat";
		JSONObject requestJson = new JSONObject();
		
		requestJson.put("RETURN_URL", "http://makepinbackdev4env-env.eba-mdmx2i2y.ap-northeast-2.elasticbeanstalk.com:8080/");
		requestJson.put("SITE_NAME", "makepin");
		requestJson.put("SITE_URL", "http://dev.makepin.co.kr/");
		requestJson.put("RQST_CAUS_CD", "00");
		
		
		
		String param = requestJson.toString();
		
		
		OkCert cert = new OkCert();
		
		
		
		try {
			String responseString = cert.callOkCert(target, cpCd, svcName, license, param);
			JSONObject responseJson = new JSONObject(responseString);
			System.out.println(responseJson);
			System.out.println("TX_SEQ_NO: " + responseJson.get("TX_SEQ_NO"));
			System.out.println("MDL_TKN: " + responseJson.get("MDL_TKN"));
		} catch (OkCertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
