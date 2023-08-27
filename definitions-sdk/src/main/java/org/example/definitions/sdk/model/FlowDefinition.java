package org.example.definitions.sdk.model;

import com.google.cloud.dialogflow.cx.v3.Flow;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import org.example.definitions.sdk.api.ResourceDiscovery;

import java.util.List;
import java.util.function.Supplier;

/**
 * This class represents the definition of a flow along with it pages.
 * The Intents and Entity Types used in the flow definitions must be pre-provisioned.
 * Use <code>{@link ResourceDiscovery}</code> class to refer dependent flows, intents and entity types
 */
@Builder
@Getter
public class FlowDefinition {
    /**
     * Name of the flow. This acts as an identifier of a flow.
     * Changing the value will result in provisioning a new flow leaving the old one as is
     */
    @NonNull
    private String resourceName;

    /**
     * Description for documentation purpose.
     */
    @NonNull
    private String description;

    /**
     * The new flow to be provisioned.
     * As this is not yet attached some of the fields populated by dialogflow
     * such as flow ID etc., will not be available to read.
     */
    @NonNull
    private Flow flow;

    /**
     * List of pages to be provisioned
     */
    @NonNull
    @Singular
    private List<Supplier<PageDefinition>> pageDefinitions;

}
