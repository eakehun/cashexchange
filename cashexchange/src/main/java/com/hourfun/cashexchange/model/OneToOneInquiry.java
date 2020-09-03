package com.hourfun.cashexchange.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(indexes= {@Index(name = "oneToOneInquiryTitle", unique=false, columnList = "createDate,status,title"),
		@Index(name = "oneToOneInquiryTitle", unique=false, columnList = "createDate,status,content")})
public class OneToOneInquiry {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	
	@Column(length = 1024)
	private String title;
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String userId;
	private String userName;
	private String tel;
	
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
