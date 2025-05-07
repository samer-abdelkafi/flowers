package com.springit.flowers.controller;


import com.springit.flowers.entity.Flow;
import com.springit.flowers.service.FlowRepoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/flows")
@AllArgsConstructor
public class FlowsController {

    private final FlowRepoService flowRepoService;

    @GetMapping()
    public Set<Flow> getAll() {
        return flowRepoService.getAll();
    }

    @GetMapping("/{id}")
    public Flow getById(@PathVariable("id") Integer id) {
        return flowRepoService.findById(id);
    }


    @PostMapping()
    public Flow save(@RequestBody Flow flow) {
        return flowRepoService.save(flow);
    }

    @PostMapping("/{id}/delete")
    public void delete(@PathVariable("id") Integer id) {
        flowRepoService.delete(id);
    }

}
