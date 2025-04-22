package com.springit.flowers.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FileDestination.class, name = "FILE"),
        @JsonSubTypes.Type(value = JmsDestination.class, name = "JMS")})


@Entity
@Table(name = "F_DESTINATION")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class Destination implements Serializable{

    @Serial
    private final static long serialVersionUID = 1L;

    public Destination(TypeMedia type) {
        this.type = type;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TYPE", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TypeMedia type;

    @Column(name = "DESCRIPTION")
    private String description;




    public enum TypeMedia {
        FILE, JMS
    }


}
