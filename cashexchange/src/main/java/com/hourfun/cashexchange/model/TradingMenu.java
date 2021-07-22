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
public class TradingMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	
	@Column(nullable = false, unique = true)
	private String menuName;
	
	@Column(columnDefinition = "BOOLEAN default true")
	private boolean used;
	
	@Column(columnDefinition = "TEXT")
	private String content;	
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date stopDate;
    
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
