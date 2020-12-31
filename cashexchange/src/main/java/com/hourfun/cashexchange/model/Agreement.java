package com.hourfun.cashexchange.model;


import java.io.Serializable;
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

@Data
@Entity
public class Agreement implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	@Column(columnDefinition = "BOOLEAN default false")
	private boolean  chkNecessary;
	private String title;
	private String contents;
	@Column(columnDefinition = "BOOLEAN default false")
	private boolean used;
	
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
