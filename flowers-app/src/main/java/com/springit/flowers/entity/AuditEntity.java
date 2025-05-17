package com.springit.flowers.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "F_FLOW_AUDIT")
@Getter
@Setter
public class AuditEntity implements Serializable {

    @Serial
    private final static long serialVersionUID = 20100415L;

    @EmbeddedId
    private AuditEntityId auditEntityId = new AuditEntityId();

    @Column(name = "INPUT_DATE", updatable = false)
    private Date inputDate;

    @Column(name = "UPDATE_STATUS_DATE")
    private Date updateDate;

    @Column(name = "TRNO")
    private String trno;

    @Column(name = "FILE")
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AuditEntityStatus status;

    @Column(name = "SERVER")
    private String server;

    public enum AuditEntityStatus {
        UNDELIVERED, // With no subscribers, sent to no one.
        ERROR, // Sent to Error thread
        PENDING, // Before trying to send the message
        OK // Should not be used since the line is deleted when message is sent
    }

    @Data
    @Embeddable
    public static class AuditEntityId implements java.io.Serializable {

        private static final long serialVersionUID = -4473910954917245489L;

        @Column(name = "FLOW_ID")
        protected Long flowId;

        @Column(name = "DEST_ID")
        private int destinationId;

        @Column(name = "JMS_ID")
        private String jmsMessageId;

    }

}
