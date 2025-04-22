package com.springit.flowers.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Entity
@Table(name = "F_FLOW_SUBSCRIBER")
public class FlowSubscriber implements Serializable{

    @Setter
    @JsonIgnore
    @EmbeddedId
    private SubscriberId subscriberId = new SubscriberId();

    @ManyToOne
    @JoinColumn(name = "DEST_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    private Destination destination;

    @ManyToOne
    @MapsId("flowId")
    @JoinColumn(name = "FLOW_ID", referencedColumnName = "ID")
    @JsonIgnore
    private Flow flow;

    @Setter
    @Column(name = "CONDITION")
    private String condition;


    public void setDestination(Destination destination) {
        this.destination = destination;
        Optional.ofNullable(destination)
                .ifPresent(dest -> subscriberId.setDestId(dest.getId()));
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
        Optional.ofNullable(flow)
                .ifPresent(topc -> subscriberId.setFlowId(topc.getId()));
    }

    @Embeddable
    @Getter
    @Setter
    public static class SubscriberId implements Serializable {

        @Column(name = "FLOW_ID")
        private Integer flowId;

        @Column(name = "DEST_ID")
        private Integer destId;


    }

}


