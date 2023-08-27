package org.example.services.provision.api;

import org.example.definitions.sdk.model.FlowDefinition;

public interface FlowProvisioner {
    void provision(FlowDefinition flowDefinition);
}
