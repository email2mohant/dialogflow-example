package org.example.definitions.sdk.api;

import org.example.definitions.sdk.model.FlowDefinition;

/**
 * Marker interface to dynamically discover the flow definitions.
 */

public interface FlowDefinitionProvider {
    FlowDefinition get();
}
