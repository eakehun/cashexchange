package com.hourfun.cashexchange.service;

import org.springframework.beans.factory.annotation.Value;

import kcb.module.v3.OkCert;
import kcb.module.v3.exception.OkCertException;

public class UserVerifyService {
	
	@Value("${okcert.target}")
	private String target;
	
	public void verifyRequest() {
		String cpCd = "V47630000000";
		String svcName = "IDS_HS_POPUP_START";
		
		String license = "";
		String param = "";
		
		OkCert cert = new OkCert();
		
		
		
		try {
			cert.callOkCert(target, cpCd, svcName, license, param);
		} catch (OkCertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
