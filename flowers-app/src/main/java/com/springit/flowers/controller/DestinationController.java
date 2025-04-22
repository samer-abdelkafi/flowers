package com.springit.flowers.controller;


import com.springit.flowers.entity.Destination;
import com.springit.flowers.repo.DestinationRepo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(path = "/destinations")
@AllArgsConstructor
public class DestinationController {

    private final DestinationRepo destinationRepository;


    @GetMapping
    public Set<Destination> getAll(){
        return new HashSet<>(destinationRepository.findAll());
    }


    @PostMapping
    public Destination save(@RequestBody Destination destination){
        return destinationRepository.save(destination);
    }


}
