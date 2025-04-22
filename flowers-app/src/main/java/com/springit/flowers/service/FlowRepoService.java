package com.springit.flowers.service;

import com.springit.flowers.entity.Flow;
import com.springit.flowers.exception.NotFoundException;
import com.springit.flowers.repo.FlowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class FlowRepoService {


    private FlowRepository flowRepository;


    public Set<Flow> getAll() {
        return new HashSet<>(flowRepository.getAll());
    }

    public Flow save(Flow flow) {
        return flowRepository.save(flow);
    }

    public Flow findById(int id) {
        return flowRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found Flow with id = " + id));
    }

    public void delete(int id) {
        flowRepository.deleteById(id);
    }



}
