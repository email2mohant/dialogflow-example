package org.example.definitions.sdk.api;

import com.google.cloud.dialogflow.cx.v3.EntityType;
import org.example.definitions.sdk.model.EntityTypeDefinition;

/**
 * Marker interface to dynamically detect the <code>{@link EntityType}</code> definitions.
 */
public interface EntityTypeDefinitionProvider {
    EntityTypeDefinition get();
}
