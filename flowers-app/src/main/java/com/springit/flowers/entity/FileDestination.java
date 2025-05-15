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
@Table(name = "F_FLOW_DESTINATION_FILE")
@Getter
@Setter
@DiscriminatorValue(value = "FILE")
public class FileDestination extends Destination implements Serializable {

    public FileDestination() {
        super(TypeMedia.FILE);
    }

    @Serial
    private final static long serialVersionUID = 1L;

    @Column(name = "DIRECTORY")
    private String directory;

    @Column(name = "SERVER_NAME")
    private String serverName;

}
