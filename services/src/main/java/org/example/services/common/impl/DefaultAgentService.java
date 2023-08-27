package org.example.services.common.impl;

import com.google.cloud.dialogflow.cx.v3.Agent;
import com.google.cloud.dialogflow.cx.v3.AgentName;
import com.google.cloud.dialogflow.cx.v3.AgentsClient;
import org.example.services.common.api.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultAgentService implements AgentService {

    private final AgentsClient agentsClient;
    private final String project;
    private final String agent;
    private final String location;

    @Autowired
    public DefaultAgentService(AgentsClient agentsClient,
                               @Value("${dialogflow.project}")
                               String project,
                               @Value("${dialogflow.agent}")
                               String agent,
                               @Value("${dialogflow.location}")
                               String location) {
        this.agentsClient = agentsClient;
        this.project = project;
        this.agent = agent;
        this.location = location;
    }

    @Override
    public Agent getAgent() {
        return this.agentsClient.getAgent(AgentName.of(project,
                location, agent));
    }
}
