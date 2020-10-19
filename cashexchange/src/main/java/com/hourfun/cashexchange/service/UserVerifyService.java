package com.hourfun.cashexchange.service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import kcb.module.v3.OkCert;
import kcb.module.v3.exception.OkCertException;
import kcb.org.json.JSONObject;

@Service
public class UserVerifyService {
	private static final Logger logger = LoggerFactory.getLogger(UserVerifyService.class);

	@Value("${okcert.target}")
	private String target;

//	@Value("${okcert.license}")
//	private String license;

	public String mobileUserVerifyRequest() throws Exception{
		String cpCd = "V47630000000";
		String svcName = "IDS_HS_POPUP_START";

//		File file = ResourceUtils.getFile("classpath:license/V47630000000_IDS_01_PROD_AES_license.dat");
		
//		String license = file.getAbsolutePath();
		
		String license = "";
		
		InputStream resource = new ClassPathResource("license/V47630000000_IDS_01_PROD_AES_license.dat").getInputStream();
		
		JSONObject requestJson = new JSONObject();

		requestJson.put("RETURN_URL", "http://dev.makepin.co.kr/");
		requestJson.put("SITE_NAME", "makepin");
		requestJson.put("SITE_URL", "http://dev.makepin.co.kr/");
		requestJson.put("RQST_CAUS_CD", "00");

		String param = requestJson.toString();

		OkCert cert = new OkCert();
		cert.delLicense(license);

		try {
			
			
//			String responseString = cert.callOkCert(target, cpCd, svcName, license, param);
			String responseString = cert.callOkCert(target, cpCd, svcName, license,  param, resource);
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
