package com.hourfun.cashexchange;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hourfun.cashexchange.model.Fee;

public class DecimalTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int fee = 30;
		int requestPrice = 5000;

		BigDecimal decimalFee = new BigDecimal(fee * 0.01).setScale(2,
				RoundingMode.HALF_EVEN);

		BigDecimal decimalPrice = new BigDecimal(requestPrice);
		BigDecimal decimalCalcFee = decimalPrice.multiply(decimalFee).setScale(0, RoundingMode.HALF_EVEN);


		int intCalPrice = decimalPrice.subtract(decimalCalcFee).intValue();
		
		System.out.println(intCalPrice);

	}

}
