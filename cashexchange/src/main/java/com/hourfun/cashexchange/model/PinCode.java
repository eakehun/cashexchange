package com.hourfun.cashexchange.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Data
public class PinCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;

	@Column(unique = true, nullable = false)
	private String pinCode;
	
	private long tradingIdx;

	private String company;
	
	private String message;

	private String status;

	private int price;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String tel;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String fees;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int completePrice;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date withdrawCompleteDate;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String accountNum;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String accountName;
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

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
