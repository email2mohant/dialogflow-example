package org.example.services.common.api;

import com.google.cloud.dialogflow.cx.v3.EntityType;
import org.example.definitions.sdk.model.EntityTypeDefinition;

import java.util.Optional;

public interface EntityTypeService {
    Optional<EntityType> getEntityType(String entityTypeName);

    EntityType create(EntityTypeDefinition entityTypeDefinition);

    void delete(String entityTypeName);
}
