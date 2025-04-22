package com.springit.flowers.entity;

import com.springit.flowers.util.ErrorStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "F_FLOW_ERROR")
@Entity
public class ErrorEntity {

    @EmbeddedId
    private ErrorEntityId errorEntityId;

    @Column(name = "ERROR_DATE")
    private Date errorDate;

    @Column(name = "OK_DATE")
    private Date okDate;

    @Column(name = "FILE")
    private String filePath;

    @Column(name = "IS_BYTES")
    private boolean bytesArray;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ErrorStatus status;

    @Column(name = "SERVER")
    private String server;

    @Transient
    private Object payload;



}
