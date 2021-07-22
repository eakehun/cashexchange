package com.hourfun.cashexchange.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import lombok.Data;

@Data
@Entity
@Table(indexes= {@Index(name = "oneToOneInquiryTitle", unique=false, columnList = "createDate,status,title"),
		@Index(name = "oneToOneInquiryStatus", unique=false, columnList = "createDate,status")})
public class OneToOneInquiry {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	
	private String title;
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String userId;
	private String userName;
	private String tel;
	
	@Enumerated(EnumType.STRING)
	private OneToOneInquiryType status;
	
	@javax.persistence.Transient
	private List<OneToOneInquiryResponse> oneInquiryResponseList = new ArrayList<OneToOneInquiryResponse>();
	
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseDate;
    
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
