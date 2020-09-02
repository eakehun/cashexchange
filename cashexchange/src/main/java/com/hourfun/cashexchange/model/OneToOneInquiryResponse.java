package com.hourfun.cashexchange.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data

public class OneToOneInquiryResponse {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	
	@ManyToOne(targetEntity = OneToOneInquiry.class, fetch = FetchType.LAZY, 
			optional = false)
    @JoinColumn(name = "parentIdx", referencedColumnName = "idx")
    @JsonBackReference
	private OneToOneInquiry oneToOneInquiry;
	@Column(columnDefinition = "TEXT")
	private String content;
	
	//admin 계정 
	private String userId;
	
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
