package com.hourfun.cashexchange.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
public class OneToOneInquiry {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	private String title;
	private String content;
	
	private String userId;
	private String userName;
	
	@Enumerated(EnumType.STRING)
	private OneToOneInquiryType status;
	
	
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
