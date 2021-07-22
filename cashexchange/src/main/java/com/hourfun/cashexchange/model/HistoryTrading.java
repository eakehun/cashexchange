package com.hourfun.cashexchange.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Data
public class HistoryTrading {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	private String device;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String createDateString;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String withdrawCompleteDateString;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String accountNum;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String accountName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createDate;
	
	private Date pinCompleteDate;
	
	private Date withdrawCompleteDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="tradingIdx")
    private Collection<HistoryPinCode> pincode;

	@PreUpdate
	protected void updateDate() {
		updateDate = new Date();
	}

	@PrePersist
	protected void createDate() {
		createDate = new Date();
		updateDate = new Date();
	}
}
