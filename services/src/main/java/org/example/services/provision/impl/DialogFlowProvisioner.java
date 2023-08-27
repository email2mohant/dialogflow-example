package org.example.services.provision.impl;

import com.google.cloud.dialogflow.cx.v3.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.definitions.sdk.model.FlowDefinition;
import org.example.definitions.sdk.model.PageDefinition;
import org.example.services.common.api.FlowService;
import org.example.services.common.api.PageService;
import org.example.services.provision.api.FlowProvisioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DialogFlowProvisioner implements FlowProvisioner {

    private final FlowService flowService;
    private final PageService pageService;


    @Autowired
    public DialogFlowProvisioner(
            FlowService flowService,
            PageService pageService
    ) {
        this.flowService = flowService;
        this.pageService = pageService;
    }


    @Override
    public void provision(FlowDefinition flowDefinition) {
        provisionFlow(flowDefinition);
    }

    private void provisionFlow(FlowDefinition flowDefinition) {
        FlowProvisionContext context = FlowProvisionContext.builder().flowDefinition(flowDefinition).build();
        createFlow(context);
        createPages(context);
        validateFlow(context);
    }

    /**
     * Provisions a new flow.
     * If a flow already exists it will delete and re-provision the flow.
     * <p>
     * Note: If this flow is already being referred by another flow then this flow has to be force deleted.
     * Force Deletion is strongly discouraged as it will cause some of the cross flow transitions orphaned.
     * TODO: Identify how to restore the dependencies post re-provision.
     * <p>
     * Note: There is no elegant updateFlow in the client library. Instead there is an option to merge the changes.
     * But the merge is designed poorly due to which some of the fields are duplicated (example page routes ..)
     * So for cleaner deployment prefer to choose delete-and-re-crete over merge for deterministic behaviour.
     *
     * @return
     */
    private void createFlow(FlowProvisionContext context) {
        FlowDefinition flowDefinition = context.getFlowDefinition();
        Optional<Flow> existingFlow = this.flowService.getFlow(flowDefinition
                .getResourceName());
        if (existingFlow.isPresent()) {
            Flow flow = existingFlow.get();
            log.warn("A flow already exists with name {} and path {} . This will be deleted ", flow.getDisplayName(), flow.getName());
            this.flowService.deleteFlow(flow.getName());
        }
        Flow flow = this.flowService.createFlow(flowDefinition);
        log.info("A new flow has provisioned with name {} and path {}", flow.getDisplayName(), flow.getName());
        context.setProvisionedFlow(flow);
    }

    private void validateFlow(FlowProvisionContext context) {
        FlowValidationResult flowValidationResult = this.flowService.validateFlow(context.getProvisionedFlow());
        if (flowValidationResult.getValidationMessagesCount() > 0) {
            log.info("************** Validation Results ****************");
            flowValidationResult.getValidationMessagesList().stream().forEach(validationMessage -> {
                List<String> resourceNamesList = validationMessage.getResourceNamesList()
                        .stream()
                        .map(resourceName -> resourceName.getDisplayName())
                        .toList();
                if (validationMessage.getSeverity() == ValidationMessage.Severity.ERROR) {

                    log.error("{} | {} | {}", validationMessage.getResourceType(), resourceNamesList, validationMessage.getDetail());
                } else {
                    log.error("{} | {} | {}", validationMessage.getResourceType(), resourceNamesList, validationMessage.getDetail());
                }
            });
            log.info("**************************************************");
        }
        context.setFlowValidationResult(flowValidationResult);
    }

    private void setupInitialTransition(FlowProvisionContext context, Page page) {

        this.flowService.updateFlow(
                Flow.newBuilder().mergeFrom(context.getProvisionedFlow())
                        .addAllTransitionRoutes(List.of(TransitionRoute.newBuilder()
                                .setCondition("true")
                                .setTargetPage(page.getName())
                                .setTriggerFulfillment(Fulfillment.newBuilder()
                                        .addMessages(ResponseMessage.newBuilder()
                                                .build())
                                        .build())
                                .build()))
                        .build());
    }


    private void createPages(FlowProvisionContext context) {
        List<Page> pages = context.getFlowDefinition()
                .getPageDefinitions()
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                })).map(pageDefinition -> this.createPage(context, pageDefinition.get()))
                .collect(Collectors.toList());
        context.setPages(pages);
    }

    /**
     * TODO:
     * A page can be defined to transition to an another page in the same flow.
     * Traverse the page dependency graph and provision the dependent pages first recursively.
     * Note: It's common a flow can contain loops in the page dependencies so the provisioning
     * logic should mark and skip the pages already provisioned.
     *

     * @param pageDefinition
     * @return
     */
    private Page createPage(FlowProvisionContext context, PageDefinition pageDefinition) {
        try {
            Flow flow = context.getProvisionedFlow();
            Page page = this.pageService.createPage(flow, pageDefinition);
            log.info("A new page has been created with name {} and path {}", page.getDisplayName(), page.getName());
            if (pageDefinition.isStartPage()) {
                setupInitialTransition(context, page);
            }
            return page;
        } catch (Exception e) {
            log.error("Error while provision page: {} ", pageDefinition.getPage().getDisplayName(), e);
            throw new RuntimeException(e);
        }

    }

    @Data
    @Builder
    private static class FlowProvisionContext {
        private final FlowDefinition flowDefinition;
        private Flow provisionedFlow;
        private FlowValidationResult flowValidationResult;
        private List<Page> pages;

        String getProvisionedPageByName(String name) {
            return this.pages.stream().filter(page -> page.getDisplayName()
                            .equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow()
                    .getName();
        }

           }


}
