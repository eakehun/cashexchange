package com.hourfun.cashexchange.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.hourfun.cashexchange.model.UserMobileVerify;
import com.hourfun.cashexchange.model.Users;

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

	public UserMobileVerify mobileUserVerifyRequest() throws Exception{

		UserMobileVerify userMobileVerify = new UserMobileVerify();

		String cpCd = "V47630000000";
		String svcName = "IDS_HS_POPUP_START";
		String siteName = "makepin";

//		File file = ResourceUtils.getFile("classpath:license/V47630000000_IDS_01_PROD_AES_license.dat");

//		String license = file.getAbsolutePath();

		String license = "";

		

		JSONObject requestJson = new JSONObject();

		requestJson.put("RETURN_URL", "http://dev.makepin.co.kr/signUp/signUpUserFrom");
		requestJson.put("SITE_NAME", siteName);
		requestJson.put("SITE_URL", "http://dev.makepin.co.kr/");
		requestJson.put("RQST_CAUS_CD", "00");

		String param = requestJson.toString();

		OkCert cert = new OkCert();

		userMobileVerify.setTc("kcb.oknm.online.safehscert.popup.cmd.P931_CertChoiceCmd");
		userMobileVerify.setCp_cd(cpCd);
		userMobileVerify.setSite_name(siteName);

		try {
			InputStream resource = new ClassPathResource("license/V47630000000_IDS_01_PROD_AES_license.dat")
					.getInputStream();
//			String responseString = cert.callOkCert(target, cpCd, svcName, license, param);
			String responseString = cert.callOkCert(target, cpCd, svcName, license, param, resource);
			JSONObject responseJson = new JSONObject(responseString);
//			logger.error("responseString: " + responseString);
//			logger.error("RSLT_CD: " + responseJson.get("RSLT_CD"));
//			logger.error("RSLT_MSG: " + responseJson.get("RSLT_MSG"));
//			logger.error("TX_SEQ_NO: " + responseJson.get("TX_SEQ_NO"));
//			logger.error("MDL_TKN: " + responseJson.get("MDL_TKN"));
			if (responseJson.has("MDL_TKN")) {
				userMobileVerify.setMdl_tkn(responseJson.getString("MDL_TKN"));
				return userMobileVerify;
			} else {
				throw new IllegalArgumentException(responseJson.getString("RSLT_CD") + " - " + responseJson.getString("RSLT_MSG"));
			}
		} catch (OkCertException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

	}

	public Users mobileUserVerifyCheck(String mdl_tkn) {
		Users users = new Users();

		String cpCd = "V47630000000";
		String svcName = "IDS_HS_POPUP_RESULT";
		String siteName = "makepin";
		String license = "";
		
		JSONObject requestJson = new JSONObject();

		requestJson.put("MDL_TKN", mdl_tkn);
		String param = requestJson.toString();
		
		OkCert cert = new OkCert();
		
		try {
			InputStream	resource = new ClassPathResource("license/V47630000000_IDS_01_PROD_AES_license.dat")
					.getInputStream();
			String responseString = cert.callOkCert(target, cpCd, svcName, license, param, resource);
			
			JSONObject responseJson = new JSONObject(responseString);
			if (responseJson.has("RSLT_NAME")) {
				users.setName(responseJson.getString("RSLT_NAME"));
				users.setBirth(responseJson.getString("RSLT_BIRTHDAY"));
				users.setGender(responseJson.getString("RSLT_SEX_CD"));
				users.setForeigner(responseJson.getString("RSLT_NTV_FRNR_CD"));
				users.setCi(responseJson.getString("CI"));
				users.setMobileOperator(responseJson.getString("TEL_COM_CD"));
				users.setTel(responseJson.getString("TEL_NO"));
			} else {
				throw new IllegalArgumentException(responseJson.getString("RSLT_CD") + " - " + responseJson.getString("RSLT_MSG"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OkCertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		return users;

	}
}
