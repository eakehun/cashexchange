package com.hourfun.cashexchange.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hourfun.cashexchange.common.BankCodeEnum;
import com.hourfun.cashexchange.util.StringUtil;

@Service
public class BankService {
	
	@Value("${settle.ownercheck.url}")
	private String ownerCheckUrl;
	
	@Value("${settle.pay.url}")
	private String payUrl;
	
	@Value("${settle.crypto.aeskey}")
	private String aesKey;
	
	@Value("${settle.crypto.shakey}")
	private String shaKey;
	
	@Value("${settle.market.id}")
	private String mchtId;

	public Map<String, String> bankList(){
		Map<String, String> map = new HashMap<String, String>();
		
		for (BankCodeEnum bankCode : BankCodeEnum.values()) {
		    map.put(bankCode.getCode(), bankCode.getName());
		}
		
		return map;
		
	}
	
	public String ownerCheck(String code, String account, String name) throws Exception {

		Map<String, Object> body = new HashMap<String, Object>();
		
		Date date = new Date();
		SimpleDateFormat oidFormat = new SimpleDateFormat("yyyyMMddhhmm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");		

		String hdInfo = "SP_NA00_1.0";

		String mchtTrdNo = "OID" + oidFormat.format(date);
		String mchtCustId = "makepin";
		String aesMchtCustId = StringUtil.encryptAES256(mchtCustId, aesKey);

		String reqDt = dateFormat.format(date);
		String reqTm = timeFormat.format(date);
		String bankCd = code;

		String custAcntNo = account;
		String aesCustAcntNo = StringUtil.encryptAES256(custAcntNo, aesKey);

		String mchtCustNm = name;
		String aesMchtCustNm = StringUtil.encryptAES256(mchtCustNm, aesKey);

		String pktHash = mchtId + mchtCustId + reqDt + reqTm + custAcntNo + shaKey;
		pktHash = StringUtil.sha256(pktHash);
		
		body.put("hdInfo", hdInfo);
		body.put("mchtId", mchtId);
		body.put("mchtTrdNo", mchtTrdNo);
		body.put("mchtCustId", aesMchtCustId);
		body.put("reqDt", reqDt);
		body.put("reqTm", reqTm);
		body.put("bankCd", bankCd);
		body.put("custAcntNo", aesCustAcntNo);
		body.put("mchtCustNm", aesMchtCustNm);
		body.put("pktHash", pktHash);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE); // send the post request
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<HashMap> response = restTemplate.postForEntity(ownerCheckUrl, entity, HashMap.class);
		
		String resultCode = (String) response.getBody().get("outStatCd");
		String resultMessage = (String) response.getBody().get("outRsltMsg");
		
		if(resultCode.equals("0021")) {
			String profile = System.getProperty("spring.profiles.active");
	        if(profile != null && profile.equals("prod")) {
	        	String resultCustNm = (String) response.getBody().get("mchtCustNm");
	        	if(!name.equals(resultCustNm)) {
	        		throw new IllegalArgumentException("CI not match");
	        	}
	        }
			
			return resultMessage;
		}else {
			throw new Exception(resultMessage);
		}
		
	}
	
	public String pay(String code, String account) throws Exception {
		Map<String, Object> body = new HashMap<String, Object>();
		
		Date date = new Date();
		SimpleDateFormat oidFormat = new SimpleDateFormat("yyyyMMddhhmm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");		

		String hdInfo = "SPAY_AR0W_1.0";
		
		String mchtTrdNo = "OID" + oidFormat.format(date);
		String mchtCustId = "makepin";

		String reqDt = dateFormat.format(date);
		String reqTm = timeFormat.format(date);

		String custAcntSumry = "메이크핀";
		
		String trdAmt = "1000";

		String pktHash = mchtId + mchtTrdNo + reqDt + reqTm + code + account + trdAmt + shaKey;
		
		body.put("hdInfo", hdInfo);
		body.put("mchtId", mchtId);
		body.put("mchtTrdNo", mchtTrdNo);
		body.put("mchtCustId", StringUtil.encryptAES256(mchtCustId, aesKey));
		body.put("trdDt", reqDt);
		body.put("trdTm", reqTm);
		body.put("bankCd", code);
		body.put("custAcntNo", StringUtil.encryptAES256(account, aesKey));
		body.put("custAcntSumry", StringUtil.encryptAES256(custAcntSumry, aesKey));
		body.put("trdAmt", trdAmt);
		body.put("pktHash", StringUtil.sha256(pktHash));

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE); // send the post request
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<HashMap> response = restTemplate.postForEntity(payUrl, entity, HashMap.class);
		
		String resultCode = (String) response.getBody().get("outStatCd");
		String resultMessage = (String) response.getBody().get("outRsltMsg");
		
		if(resultCode.equals("0021")) {
			return resultMessage;
		}else {
			throw new Exception(resultMessage);
		}
		
	}
}
