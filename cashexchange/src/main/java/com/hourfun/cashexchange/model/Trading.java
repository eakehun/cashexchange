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

import lombok.Data;

@Entity
@Data
public class Trading {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	
	@Column(name = "users_idx")
	private long usersIdx;
	
	private String company;
	
	private String status;
	
	private String requestPrice;
	
	private String fees;
	
	private String comepletePrice;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="trading_idx")
    private Collection<PinCode> pincode;

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