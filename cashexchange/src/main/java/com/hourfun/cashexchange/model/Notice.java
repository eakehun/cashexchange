package com.hourfun.cashexchange.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(indexes= {@Index(name = "noticeCreateDateDisplayFixed", unique=false, columnList = "createDate,display,fixed"),
		@Index(name = "noticeCreateDate", unique=false, columnList = "createDate")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notice {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;

	private String userId;
	
	private String title;
	@Column(columnDefinition = "TEXT")
	private String content;
	@Column(columnDefinition = "BOOLEAN default true")
	private boolean display;

	@Column(columnDefinition = "BOOLEAN default true")
	private boolean fixed;
	
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

