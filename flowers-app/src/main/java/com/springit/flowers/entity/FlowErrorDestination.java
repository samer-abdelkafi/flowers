package com.springit.flowers.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;



//@Getter
//@Setter
//@Entity
//@Table(name = "STPF_FLOW_ERROR")
public class FlowErrorDestination implements Serializable {

    @JsonIgnore
    @EmbeddedId
    private ErrorDestinationId id = new ErrorDestinationId();

    @ManyToOne
    @MapsId("flowId")
    @JoinColumn(name = "FLOW_ID", referencedColumnName = "ID")
    private Flow flow;

    @ManyToOne
    @MapsId("destinationId")
    @JoinColumn(name = "DEST_ID", referencedColumnName = "ID")
    private Destination destination;

    @Column(name = "ERROR_DATE")
    private Date errorDate;

    @Column(name = "FILE")
    private String filePath;

    @Column(name = "IS_BYTES")
    private boolean bytesArray;

    @Column(name = "STATUS")
    private ErrorStatus status;

    @Column(name = "SERVER")
    private String server;


    public enum ErrorStatus {
        ERROR,
        RETRYING,
        OK
    }


    @Embeddable
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDestinationId implements Serializable {

        @Column(name = "FLOW_ID")
        private Integer flowId;

        @Column(name = "DEST_ID")
        private Integer destinationId;

        @Column(name = "JMS_ID")
        private String jmsMessageId;
    }

}
