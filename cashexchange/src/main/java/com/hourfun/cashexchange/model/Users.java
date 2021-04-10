package com.hourfun.cashexchange.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Data
public class Users implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	@Column(unique=true,nullable = false)
	private String userId;
	
	@Column(nullable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String pwd;
	
	@Column(nullable = false)
	private String auth;
	private String tel;
	private String mobileOperator;
	private String birth;
	private String gender;
	private String foreigner;
	private String email;
	private String telChkValue;
	private String accountNum;
	private String accountName;
	private String accountCode;
	private String accountStatus;
	private String backCode;
	
	private String name;
	
	private String ci;
	
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date lastLogin;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String lastDevice;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Collection<Agreement> agreementList;
	
	@Transient
	private Boolean limitOver;
	
	private Date suspendedDate;
	
	private Date withdrawDate;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private long totalTradingCount;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String totalCompletePrice;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private long monthTradingCount;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String monthCompletePrice;
	
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_agreements")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Collection<Agreement> agreements;
	
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
