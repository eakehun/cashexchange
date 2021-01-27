package com.hourfun.cashexchange.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
public class HistoryTrading {
	@Id
	private long idx;
	
	private String userId;
	
	private String userName;
	
	private String company;
	
	private String status;
	
	private String withdrawStatus;
	
	private int requestPrice;
	
	private String fees;
	
	private int comepletePrice;
	
	private String message;
	
	private String tel;
	
	private long purchaseFeePercents;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date pinCompleteDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date withdrawCompleteDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="tradingIdx")
    private Collection<HistoryPinCode> pincode;
}
