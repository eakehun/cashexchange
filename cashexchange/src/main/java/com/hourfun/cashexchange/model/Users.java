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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import lombok.Data;

@Entity
@Data
public class Users {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	@Column(unique=true,nullable = false)
	private String id;
	@Column(nullable = false)
	private String pwd;
	@Column(nullable = false)
	private String auth;
	private String tel;
	private String mobileOperator;
	private String birth;
	private String gender;
	private String email;
	private String telChkValue;
	private String accountNum;
	private String accountName;
	private String accountStatus;
	private String backCode;
	
	private String name;
	
	private String ci;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_agreements")
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
