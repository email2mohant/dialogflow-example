package org.example.definitions.sdk.model;

import com.google.cloud.dialogflow.cx.v3.EntityType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class EntityTypeDefinition {
    @NonNull
    private String resourceName;
    @NonNull
    private String description;
    @NonNull
    private EntityType entityType;
}
