package org.example;

import org.example.definitions.sdk.api.EntityTypeDefinitionProvider;
import org.example.definitions.sdk.api.FlowDefinitionProvider;
import org.example.definitions.sdk.model.EntityTypeDefinition;
import org.example.definitions.sdk.model.FlowDefinition;
import org.example.services.provision.api.EntityTypeProvisioner;
import org.example.services.provision.api.FlowProvisioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication(scanBasePackages = {"org.example"})

public class DialogFlowDefinitions implements CommandLineRunner {


    @Autowired
    private List<EntityTypeDefinitionProvider> entityTypeDefinitionProviders;

    @Autowired
    private EntityTypeProvisioner entityTypeProvisioner;

    @Autowired
    private List<FlowDefinitionProvider> flowDefinitionProviders;

    @Autowired
    private FlowProvisioner flowProvisioner;

    @Override
    public void run(String... args) {
//        provisionEntities();
        provisionFlows();
    }

    private void provisionEntities() {
        entityTypeDefinitionProviders.stream().forEach(entityTypeDefinitionProvider -> {
            EntityTypeDefinition entityTypeDefinition = entityTypeDefinitionProvider.get();
            this.entityTypeProvisioner.provision(entityTypeDefinition);
        });
    }

    private void provisionFlows() {
        flowDefinitionProviders.stream().forEach(flowDefinitionProvider -> {
            FlowDefinition flowDefinition = flowDefinitionProvider.get();
            this.flowProvisioner.provision(flowDefinition);
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(DialogFlowDefinitions.class, args);
    }


}
