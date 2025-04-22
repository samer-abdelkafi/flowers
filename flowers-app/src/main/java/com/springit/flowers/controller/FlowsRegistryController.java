package com.springit.flowers.controller;


import com.springit.flowers.entity.Flow;
import com.springit.flowers.service.FlowRegistrationService;
import com.springit.flowers.service.FlowService;
import com.springit.flowers.service.FlowRepoService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/flows/registry")
@AllArgsConstructor
public class FlowsRegistryController {


    private FlowRegistrationService flowRegistrationService;

    private FlowRepoService flowRepoService;

    private FlowService flowService;

    @GetMapping("/ids")
    public Set<Integer> getRegistryIds() {
        return flowRegistrationService.getRegistry().keySet().stream()
                .map(flowId -> flowId.split("_"))
                .map(strArray -> strArray[0])
                .filter(StringUtils::isNumeric)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    @GetMapping
    public Set<String> getRegistry() {
        return flowRegistrationService.getRegistry().keySet();
    }


    @PostMapping("/{id}/unregister")
    public Set<String> unegister(@PathVariable("id") @NonNull Integer id) {
        return flowRegistrationService.getRegistry().keySet().stream()
                .filter(flowId -> flowId.startsWith(id + "_"))
                .peek(flowRegistrationService::unregisterFlow)
                .collect(Collectors.toSet());
    }

    @PostMapping("/{id}/register")
    public Set<String> register(@PathVariable("id") @NonNull Integer id) {
        Flow flow = flowRepoService.findById(id);
        flowService.createFlow(flow);
        return flowRegistrationService.getRegistry().keySet().stream()
                .filter(flowId -> flowId.startsWith(id + "_"))
                .collect(Collectors.toSet());
    }


}
