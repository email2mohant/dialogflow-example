package org.example.definition.impl;

import com.google.cloud.dialogflow.cx.v3.AgentName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component

public class DialogFlowAgent {
    private final String project;
    private final String agent;
    private final String location;

    @Autowired
    public DialogFlowAgent(@Value("${dialogflow.project}") String project,
                           @Value("${dialogflow.agent}") String agent,
                           @Value("${dialogflow.location}") String location){

        this.project = project;
        this.agent = agent;
        this.location = location;
    }
    public String getAgentName(){
        return AgentName.newBuilder()
                    .setProject(project)
                    .setLocation(location)
                    .setAgent(agent)
                .build().toString();
    }


    public String getProject() {
        return project;
    }

    public String getAgent() {
        return agent;
    }

    public String getLocation() {
        return location;
    }
}
