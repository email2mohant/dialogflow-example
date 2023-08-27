package org.example.services.common.impl;

import com.google.cloud.dialogflow.cx.v3.Flow;
import org.example.definitions.sdk.api.ResourceDiscovery;
import org.example.definitions.sdk.exception.NoResourceFoundException;
import org.example.services.common.api.EntityTypeService;
import org.example.services.common.api.FlowService;
import org.example.services.common.api.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class DialogFlowResourceDiscovery implements ResourceDiscovery {
    private final FlowService flowService;
    private final PageService pageService;
    private final EntityTypeService entityTypeService;


    @Autowired
    public DialogFlowResourceDiscovery(FlowService flowService,
                                       PageService pageService, EntityTypeService entityTypeService) {
        this.flowService = flowService;
        this.pageService = pageService;
        this.entityTypeService = entityTypeService;
    }

    @Override
    public String discoverFlow(String flowName) {
        return this.flowService.getFlow(flowName).orElseThrow(() -> new NoResourceFoundException(flowName)).getName();
    }

    @Override
    public String discoverPage(String flowName, String pageName) {
        Flow flow = this.flowService.getFlow(flowName).orElseThrow(() -> new NoResourceFoundException(flowName));
        return this.pageService.getPage(flow, pageName).orElseThrow(() -> new NoResourceFoundException(pageName)).getName();
    }

    @Override
    public String discoverEntityType(String entityTypeName) {
        return this.entityTypeService.getEntityType(entityTypeName).orElseThrow(() -> new NoResourceFoundException(entityTypeName)).getName();
    }
}
