package org.example.services.common.impl;

import com.google.cloud.dialogflow.cx.v3.Agent;
import com.google.cloud.dialogflow.cx.v3.CreateEntityTypeRequest;
import com.google.cloud.dialogflow.cx.v3.EntityType;
import com.google.cloud.dialogflow.cx.v3.EntityTypesClient;
import org.example.definitions.sdk.model.EntityTypeDefinition;
import org.example.services.common.api.AgentService;
import org.example.services.common.api.EntityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Service
public class DefaultEntityTypeService implements EntityTypeService {
    private final AgentService agentService;
    private final EntityTypesClient entityTypesClient;

    @Autowired
    public DefaultEntityTypeService(AgentService agentService, EntityTypesClient entityTypesClient) {
        this.agentService = agentService;
        this.entityTypesClient = entityTypesClient;
    }

    @Override
    public Optional<EntityType> getEntityType(String entityTypeName) {
        Agent agent = this.agentService.getAgent();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.entityTypesClient.listEntityTypes(agent.getName())
                        .iterateAll()
                        .iterator(), 0), false)
                .filter(entityType -> entityType.getDisplayName().equalsIgnoreCase(entityTypeName))
                .findFirst();

    }

    @Override
    public EntityType create(EntityTypeDefinition entityTypeDefinition) {
        Agent agent = this.agentService.getAgent();
        return this.entityTypesClient.createEntityType(CreateEntityTypeRequest.newBuilder()
                .setParent(agent.getName())
                .setEntityType(EntityType.newBuilder()
                        .mergeFrom(entityTypeDefinition.getEntityType())
                        .setDisplayName(entityTypeDefinition.getResourceName())
                        .build())
                .build());
    }

    @Override
    public void delete(String entityTypeName) {
        this.entityTypesClient.deleteEntityType(entityTypeName);
    }
}
