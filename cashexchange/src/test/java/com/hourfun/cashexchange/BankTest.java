package com.hourfun.cashexchange;

import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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
		String url = "https://tbnpay.settlebank.co.kr/v1/api/auth/acnt/ownercheck1";

		String aesKey = "SETTLEBANKISGOODSETTLEBANKISGOOD";
		String shaKey = "ST190808090913247723";

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
		
		Date date = new Date();
		SimpleDateFormat oidFormat = new SimpleDateFormat("yyyyMMddhhmm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
		

		String hdInfo = "SP_NA00_1.0";
		String mchtId = "M20B2449";

		String mchtTrdNo = "OID" + oidFormat.format(date);
		String mchtCustId = "gnogun";
		String aesMchtCustId = encryptAES256(mchtCustId, aesKey);

		String reqDt = dateFormat.format(date);
		String reqTm = timeFormat.format(date);

		String bankCd = "003";

		String custAcntNo = "67690201671251";
		String aesCustAcntNo = encryptAES256(custAcntNo, aesKey);

		String mchtCustNm = "송근호";
		String aesMchtCustNm = encryptAES256(mchtCustNm, aesKey);

		String custIp = "121.165.86.243";

		String pktHash = mchtId + mchtCustId + reqDt + reqTm + custAcntNo + shaKey;
		pktHash = sha256(pktHash);
		
		body.set("hdInfo", hdInfo);
		body.set("mchtId", mchtId);
		body.set("mchtTrdNo", mchtTrdNo);
		body.set("aesMchtCustId", aesMchtCustId);
		body.set("reqDt", reqDt);
		body.set("reqTm", reqTm);
		body.set("bankCd", bankCd);
		body.set("aesCustAcntNo", aesCustAcntNo);
		body.set("aesMchtCustNm", aesMchtCustNm);
		body.set("custIp", custIp);
		body.set("pktHash", pktHash);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE); // send the post request
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		
		System.out.println(response);

	}

	public static String encryptAES256(String msg, String key) throws Exception {

		SecureRandom random = new SecureRandom();

		byte bytes[] = new byte[20];

		random.nextBytes(bytes);

		byte[] saltBytes = bytes;

		// Password-Based Key Derivation function 2

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		// 70000번 해시하여 256 bit 길이의 키를 만든다.

		PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);

		SecretKey secretKey = factory.generateSecret(spec);

		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

		// 알고리즘/모드/패딩

		// CBC : Cipher Block Chaining Mode

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, secret);

		AlgorithmParameters params = cipher.getParameters();

		byte[] encrypted = cipher.doFinal(msg.getBytes());
		return byteToHexString(encrypted);

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


}
