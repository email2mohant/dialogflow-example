package org.example.services.common.api;


import com.google.cloud.dialogflow.cx.v3.Flow;
import com.google.cloud.dialogflow.cx.v3.FlowValidationResult;
import org.example.definitions.sdk.model.FlowDefinition;

import java.util.Optional;

public interface FlowService {
    Optional<Flow> getFlow(String flowName);

    Flow createFlow(FlowDefinition flowDefinition);

    void deleteFlow(String flowName);

    Flow updateFlow(Flow flow);

    FlowValidationResult validateFlow(Flow flow);
}
