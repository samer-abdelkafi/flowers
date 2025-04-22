package com.springit.flowers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ErrorEntityId implements Serializable {

    @Column(name = "FLOW_ID")
    private int flowId;

    @Column(name = "DEST_ID")
    private int destinationId;

    @Column(name = "JMS_ID")
    private String jmsMessageId;
}
