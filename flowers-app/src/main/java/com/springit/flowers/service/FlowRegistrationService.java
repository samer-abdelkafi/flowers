package com.springit.flowers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FlowRegistrationService {

    private final IntegrationFlowContext flowContext;

    @Autowired
    public FlowRegistrationService(IntegrationFlowContext flowContext) {
        this.flowContext = flowContext;
    }

    public IntegrationFlowContext.IntegrationFlowRegistration registerFlow(String id, IntegrationFlow flow) {
        return flowContext.registration(flow).id(id).register();
    }

    public void unregisterFlow(String flowId) {
        flowContext.remove(flowId);
    }

    public Map<String, IntegrationFlowContext.IntegrationFlowRegistration> getRegistry() {
        return flowContext.getRegistry();
    }
}
