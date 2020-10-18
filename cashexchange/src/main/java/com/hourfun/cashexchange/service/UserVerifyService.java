package com.hourfun.cashexchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kcb.module.v3.OkCert;
import kcb.module.v3.exception.OkCertException;
import kcb.org.json.JSONObject;

@Service
public class UserVerifyService {
	private static final Logger logger = LoggerFactory.getLogger(UserVerifyService.class);

	@Value("${okcert.target}")
	private String target;

	@Value("${okcert.license}")
	private String license;

	public String mobileUserVerifyRequest() throws Exception{
		String cpCd = "V47630000000";
		String svcName = "IDS_HS_POPUP_START";

		JSONObject requestJson = new JSONObject();

		requestJson.append("RETURN_URL", "http://dev.makepin.co.kr/");
		requestJson.append("SITE_NAME", "makepin");
		requestJson.append("SITE_URL", "http://dev.makepin.co.kr/");
		requestJson.append("RQST_CAUS_CD", "00");

		String param = requestJson.toString();

		OkCert cert = new OkCert();		

		try {
			String responseString = cert.callOkCert(target, cpCd, svcName, license, param);
			JSONObject responseJson = new JSONObject(responseString);
			logger.error("responseString: " + responseString);
//			logger.error("RSLT_CD: " + responseJson.get("RSLT_CD"));
//			logger.error("RSLT_MSG: " + responseJson.get("RSLT_MSG"));
//			logger.error("TX_SEQ_NO: " + responseJson.get("TX_SEQ_NO"));
//			logger.error("MDL_TKN: " + responseJson.get("MDL_TKN"));
			if(responseJson.has("MDL_TKN")) {
				return (String) responseJson.get("MDL_TKN");
			}else {
				return responseString;
			}
		} catch (OkCertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
			
		}
		
	}
}
