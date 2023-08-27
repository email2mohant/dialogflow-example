package org.example.services.common.impl;

import com.google.cloud.dialogflow.cx.v3.*;
import org.example.definitions.sdk.model.FlowDefinition;
import org.example.services.common.api.AgentService;
import org.example.services.common.api.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Service
public class DefaultFlowService implements FlowService {
    private final FlowsClient flowsClient;
    private final AgentService agentService;

    @Autowired
    public DefaultFlowService(FlowsClient flowsClient, AgentService agentService) {
        this.flowsClient = flowsClient;
        this.agentService = agentService;
    }


    public Optional<Flow> getFlow(String flowName) {
        String agentName = this.agentService.getAgent().getName();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.flowsClient.listFlows(agentName)
                        .iterateAll()
                        .iterator(), 0), false)
                .filter(flow -> flow.getDisplayName().equalsIgnoreCase(flowName))
                .findFirst();

    }

    @Override
    public Flow createFlow(FlowDefinition flowDefinition) {
        String name = this.agentService.getAgent().getName();
        Flow flow = flowDefinition.getFlow();
        return this.flowsClient
                .createFlow(CreateFlowRequest.newBuilder()
                        .setParent(name)
                        .setFlow(Flow.newBuilder()
                                .mergeFrom(flow)
                                .setDisplayName(flowDefinition.getResourceName())
                                .build())
                        .build());
    }

    @Override
    public void deleteFlow(String flowName) {
        this.flowsClient.deleteFlow(DeleteFlowRequest.newBuilder()
                .setName(flowName)
                .build());
    }


    @Override
    public Flow updateFlow(Flow flow) {
        return this.flowsClient.updateFlow(UpdateFlowRequest.newBuilder()
                        .setFlow(flow)
                .build());
    }

    @Override
    public FlowValidationResult validateFlow(Flow flow){
        return this.flowsClient.validateFlow(ValidateFlowRequest.newBuilder()
                        .setName(flow.getName())
                .build());
    }
}
