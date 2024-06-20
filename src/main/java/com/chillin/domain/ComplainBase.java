package com.chillin.domain;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class ComplainBase {

    private String reason;
    private String detail;
    private Boolean blind;
    private Boolean complete;

}
