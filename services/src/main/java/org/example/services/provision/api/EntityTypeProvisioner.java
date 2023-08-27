package org.example.services.provision.api;

import org.example.definitions.sdk.model.EntityTypeDefinition;

public interface EntityTypeProvisioner {
    void provision(EntityTypeDefinition entityTypeDefinition);
}
