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

import lombok.Data;

@Entity
@Data
public class HistoryPinCode {

	@Id
	private long idx;

	@Column(unique = true, nullable = false)
	private String pinCode;
	
	private long tradingIdx;

	private String company;
	
	private String message;

	private String status;

	private int price;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

}
