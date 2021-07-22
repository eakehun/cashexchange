package com.hourfun.cashexchange;

import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class BankTest {
	/*
	 * 개읶정보 알고리즘 AES-256/ECB/PKCS5Padding Base64 Encoding 대상 필드 담당자명, 유선번호, 휴대폰번호,
	 * 이메읷, 예금주명, 계좌번호 등 (암호화 대상 필드는 개별 API 의 요청 필드 규격의 비고띾에 명시됩니다) 테스트베드 키
	 * SETTLEBANKISGOODSETTLEBANKISGOOD(32byte)
	 * 
	 * 위변조 알고리즘 SHA-256 Hex Encoding 테스트베드 키 ST190808090913247723(20byte)
	 */

	public static void main(String[] args) throws Exception {
		

//		ownerCheck();
		pay();
//		maintenanceCheck();
		
		
	}
	
	public static void ownerCheck() throws Exception {
//		String url = "https://tbnpay.settlebank.co.kr/v1/api/auth/acnt/ownercheck1";
		String url = "https://npay.settlebank.co.kr/v1/api/auth/acnt/ownercheck1";

//		String aesKey = "SETTLEBANKISGOODSETTLEBANKISGOOD";
//		String shaKey = "ST190808090913247723";
		
		String aesKey = "DoWCUhK7MkF2I80u15Ws57iLHmyjDCu3";
		String shaKey = "ST2012141735025387924";

		Map<String, Object> body = new HashMap<String, Object>();
		
		Date date = new Date();
		SimpleDateFormat oidFormat = new SimpleDateFormat("yyyyMMddhhmm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");		

		String hdInfo = "SP_NA00_1.0";
//		String mchtId = "M20B2449";
		String mchtId = "M20B1502";

		String mchtTrdNo = "OID" + oidFormat.format(date);
		String mchtCustId = "makepin";
		String aesMchtCustId = encryptAES256(mchtCustId, aesKey);

		String reqDt = dateFormat.format(date);
		String reqTm = timeFormat.format(date);
		String bankCd = "004";

		String custAcntNo = "67690201671251";
		String aesCustAcntNo = encryptAES256(custAcntNo, aesKey);

		String mchtCustNm = "송근호";
		String aesMchtCustNm = encryptAES256(mchtCustNm, aesKey);

//		String custIp = "121.165.82.110";

		String pktHash = mchtId + mchtCustId + reqDt + reqTm + custAcntNo + shaKey;
		pktHash = sha256(pktHash);
		
		body.put("hdInfo", hdInfo);
		body.put("mchtId", mchtId);
		body.put("mchtTrdNo", mchtTrdNo);
		body.put("mchtCustId", aesMchtCustId);
		body.put("reqDt", reqDt);
		body.put("reqTm", reqTm);
		body.put("bankCd", bankCd);
		body.put("custAcntNo", aesCustAcntNo);
		body.put("mchtCustNm", aesMchtCustNm);
//		body.put("custIp", custIp);
		body.put("pktHash", pktHash);
		

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE); // send the post request
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		
		ResponseEntity<HashMap> response = restTemplate.postForEntity(url, entity, HashMap.class);
		
		System.out.println(response);
	}
	
	public static void pay() throws Exception {
//		String url = "https://tbnpay.settlebank.co.kr/v1/api/pay/rmt";
		String url = "https://npay.settlebank.co.kr/v1/api/pay/rmt";

//		String aesKey = "SETTLEBANKISGOODSETTLEBANKISGOOD";
//		String shaKey = "ST190808090913247723";
		String aesKey = "DoWCUhK7MkF2I80u15Ws57iLHmyjDCu3";
		String shaKey = "ST2012141735025387924";

		Map<String, Object> body = new HashMap<String, Object>();
		
		Date date = new Date();
		SimpleDateFormat oidFormat = new SimpleDateFormat("yyyyMMddhhmm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");		

		String hdInfo = "SPAY_AR0W_1.0";
		
//		String mchtId = "M20B2449";
		String mchtId = "M20B1502";

		String mchtTrdNo = "OID" + oidFormat.format(date);
		String mchtCustId = "makepin";

		String reqDt = dateFormat.format(date);
		String reqTm = timeFormat.format(date);
		String bankCd = "004";

		String custAcntNo = "67690201671251";

		String custAcntSumry = "원투";
		
		String trdAmt = "100";


		String pktHash = mchtId + mchtTrdNo + reqDt + reqTm + bankCd + custAcntNo + trdAmt + shaKey;
		
		body.put("hdInfo", hdInfo);
		body.put("mchtId", mchtId);
		body.put("mchtTrdNo", mchtTrdNo);
		body.put("mchtCustId", encryptAES256(mchtCustId, aesKey));
		body.put("trdDt", reqDt);
		body.put("trdTm", reqTm);
		body.put("bankCd", bankCd);
		body.put("custAcntNo", encryptAES256(custAcntNo, aesKey));
		body.put("custAcntSumry", encryptAES256(custAcntSumry, aesKey));
		body.put("trdAmt", trdAmt);
		body.put("pktHash", sha256(pktHash));

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE); // send the post request
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<HashMap> response = restTemplate.postForEntity(url, entity, HashMap.class);
		
		System.out.println(response);
	}
	
	public static void maintenanceCheck() throws Exception {
//		String url = "https://tbnpay.settlebank.co.kr/v1/api/bank/timecheck";
		String url = "https://npay.settlebank.co.kr/v1/api/bank/timecheck";

//		String aesKey = "SETTLEBANKISGOODSETTLEBANKISGOOD";
//		String shaKey = "ST190808090913247723";
		
		String aesKey = "DoWCUhK7MkF2I80u15Ws57iLHmyjDCu3";
		String shaKey = "ST2012141735025387924";

		Map<String, Object> body = new HashMap<String, Object>();
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");		

		String hdInfo = "SPAY_TP00_1.0";
		
		String pktDivCd = "NA";
		
//		String mchtId = "M20B2449";
		String mchtId = "M20B1502";

		String reqDt = dateFormat.format(date);
		String reqTm = timeFormat.format(date);
		String bankCd = "004";

		String pktHash = mchtId + reqDt + reqTm + bankCd + shaKey;
		
		body.put("hdInfo", hdInfo);
		body.put("pktDivCd", pktDivCd);
		body.put("mchtId", mchtId);
		body.put("reqDt", reqDt);
		body.put("reqTm", reqTm);
		body.put("bankCd", bankCd);
		body.put("pktHash", sha256(pktHash));

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE); // send the post request
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<HashMap> response = restTemplate.postForEntity(url, entity, HashMap.class);
		
		System.out.println(response);
	}

	public static String encryptAES256(String msg, String key) throws Exception {

		SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

		// CBC : Cipher Block Chaining Mode

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, secret);

		byte[] encrypted = cipher.doFinal(msg.getBytes());
		return byteToBase64String(encrypted);

	}

	public static String sha256(String msg)  throws NoSuchAlgorithmException {

	    MessageDigest md = MessageDigest.getInstance("SHA-256");

	    md.update(msg.getBytes());

	    return byteToHexString(md.digest());

	}
	
	public static String byteToHexString(byte[] data) {

	    StringBuilder sb = new StringBuilder();

	    for(byte b : data) {

	        sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

	    }

	    return sb.toString();

	}
	
	public static String byteToBase64String(byte[] data) {
		Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(data);
		
		return new String(encodedBytes);
	}


}
