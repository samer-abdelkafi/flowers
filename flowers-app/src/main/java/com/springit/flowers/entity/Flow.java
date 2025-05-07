package com.springit.flowers.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "F_FLOW")
public class Flow implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "F_FLOW_PUBLISHER",
            joinColumns = @JoinColumn(name = "FLOW_ID"),
            inverseJoinColumns = @JoinColumn(name = "DEST_ID"))
    private Set<Destination> publishers = new HashSet<>();



    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FlowSubscriber> subscribers = new HashSet<>();


    @Column(name = "ATTRIBUTES")
    private String attributesStringFormat;

    @Column(name = "FILTER_TYPE")
    private String filterTypesStringFormat;

    @Column(name = "ACTIVE")
    private boolean active;


    public void setSubscribers(Set<FlowSubscriber> subscribers) {
        this.subscribers = subscribers;
        Optional.ofNullable(this.subscribers).stream()
                .flatMap(Collection::stream)
                .forEach(flowSubscriber -> flowSubscriber.setFlow(this));
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Flow)
                && o.hashCode() == this.hashCode();
    }


}
