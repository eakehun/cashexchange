package com.hourfun.cashexchange.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BankCodeEnum {
	
	KDB("산업은행", "002"),
	IBK("기업은행", "003"),
	KB("국민은행", "004"),
	SH("수협은행", "007"),
	NH("농협은행", "011"),
	NHC("농협중앙회", "012"),
	WOORI("우리은행", "020"),
	SCJAEIL("SC제일은행", "023"),
	CITI("한국씨티은행", "027"),
	DGB("대구은행", "031"),
	BSBNK("부산은행", "032"),
	GWANGJU("광주은행", "034"),
	JEJU("제주은행", "035"),
	JNB("전북은행", "037"),
	KNBNK("경남은행", "039"),
	MG("새마을금고중앙회", "045"),
	SHINHYUP("신협중앙회", "048"),
	FSB("상호저축은행", "050"),
	HSBC("HSBC 은행", "054"),
	DEUTSCHE("도이치은행", "055"),
	JPMORGAN("제이피모간체이스은행", "057"),
	BOA("BOA 은행", "060"),
	ICBC("중국공상은행", "062"),
	SJ("산림조합중앙회", "064"),
	POST("우체국", "071"),
	KEB("KEB 하나은행", "081"),
	SHINHAN("신한은행", "088"),
	KBANK("K뱅크", "089"),
	KAKAO("카카오뱅크", "090"),
	YUANTA("유안타증권", "209"),
	KBSTOCK("KB 증권", "218"),
	MIRAEASSET("미래에셋대우", "238"),
	SAMSUNG("삼성증권", "240"),
	BANKIS("한국투자증권", "243"),
	NHQV("NH 투자증권", "247"),
	KYOBO("교보증권", "261"),
	HIIB("하이투자증권", "262"),
	HYUNDAI("현대차투자증권", "263"),
	KIWOOM("키움증권", "264"),
	EBEST("이베스트투자증권", "265"),
	SKS("SK 증권", "266"),
	DAISHIN("대신증권", "267"),
	HANHWA("한화투자증권", "269"),
	HANAW("하나금융투자", "270"),
	SHINHANINVEST("신한금융투자", "278"),
	DBFI("동부증권", "279"),
	EUGENEFN("유진투자증권", "280"),
	MERITZ("메리츠종합금융증권", "287"),
	BOOKOOK("부국증권", "290"),
	SHINYOUNG("신영증권", "291"),
	CAPEFN("케이프투자증권", "292"),
	SBISB("SBI 저축은행", "103")
	;

	private String name;
	private String code;
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	
}
