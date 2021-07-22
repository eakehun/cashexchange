package com.hourfun.cashexchange.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "history")
public class History {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String service;

    private String keyword;

    private long count;

    private long lte;

    private long gte;

    private String ip;

    private String user;
    
    private String device;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @PrePersist
    protected void createDate() {
        createDate = new Date();
    }

}
