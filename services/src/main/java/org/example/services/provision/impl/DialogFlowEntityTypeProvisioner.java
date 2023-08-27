package org.example.services.provision.impl;

import com.google.cloud.dialogflow.cx.v3.EntityType;
import lombok.extern.slf4j.Slf4j;
import org.example.definitions.sdk.model.EntityTypeDefinition;
import org.example.services.common.api.AgentService;
import org.example.services.common.api.EntityTypeService;
import org.example.services.provision.api.EntityTypeProvisioner;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DialogFlowEntityTypeProvisioner implements EntityTypeProvisioner {
    private final AgentService agentService;
    private final EntityTypeService entityTypeService;

    public DialogFlowEntityTypeProvisioner(AgentService agentService, EntityTypeService entityTypeService){
        this.agentService = agentService;
        this.entityTypeService = entityTypeService;
    }
    @Override
    public void provision(EntityTypeDefinition entityTypeDefinition) {
        Optional<EntityType> entityType = this.entityTypeService.getEntityType(entityTypeDefinition.getResourceName());
        if(entityType.isPresent()){
            log.info("An EntityType already exists with name {} and path {}. This will be deleted", entityType.get().getDisplayName(), entityType.get().getName());
            this.entityTypeService.delete(entityType.get().getName());
        }
        EntityType entityType1 = this.entityTypeService.create(entityTypeDefinition);
        log.info("A new Entity Type created  with name {} and path {}. This will be deleted", entityType1.getDisplayName(), entityType1.getName());
    }
}
