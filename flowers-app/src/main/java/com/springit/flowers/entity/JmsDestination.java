package com.springit.flowers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "F_FLOW_DESTINATION_JMS")
@Getter
@Setter
@DiscriminatorValue(value = "JMS")
public class JmsDestination extends Destination  implements Serializable {

    public JmsDestination() {
        super(TypeMedia.JMS);
    }

    @Serial
    private final static long serialVersionUID = 1L;

    @Column(name = "QUEUE_NAME")
    private String queueName;

    @Column(name = "SERVER_NAME")
    private String serverName;

    private transient boolean listening;

}
