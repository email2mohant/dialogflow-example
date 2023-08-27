package org.example.definition.flow.cards;

import com.google.cloud.dialogflow.cx.v3.*;
import com.google.protobuf.NullValue;
import com.google.protobuf.Value;
import org.example.definitions.sdk.api.FlowDefinitionProvider;
import org.example.definitions.sdk.api.ResourceDiscovery;
import org.example.definitions.sdk.model.FlowDefinition;
import org.example.definitions.sdk.model.PageDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateCreditLimitFlow implements FlowDefinitionProvider {
    public static final String FLOW_NAME = "krn.flows.cards.update_credit_limit.v1";
    public static final String UPDATE_CREDIT_LIMIT_START_PAGE = "update_credit_limit_start_page";
    public static final String CAPTURE_CARD_DETAILS_PAGE = "capture_card_details_page";
    public static final String CAPTURE_NEW_CREDIT_LIMIT = "capture_new_credit_limit";
    public static final String CREATE_SERVICE_REQUEST_PAGE = "service_request_page";

    private ResourceDiscovery resourceDiscovery;

    @Autowired
    public UpdateCreditLimitFlow(ResourceDiscovery resourceDiscovery) {
        this.resourceDiscovery = resourceDiscovery;
    }

    @Override
    public FlowDefinition get() {
        return FlowDefinition.builder()
                .description("Flow to Update Credit Card Limit")
                .flow(Flow.newBuilder()
                        .build())
                .pageDefinition(this::updateCreditLimitStartPage)
                .pageDefinition(this::captureCardDetails)
                .pageDefinition(this::captureNewCreditLimit)
                .pageDefinition(this::serviceRequestPage)
                .resourceName(FLOW_NAME)
                .build();
    }

    private PageDefinition updateCreditLimitStartPage() {
        return PageDefinition.builder()
                .description("Initialized or Reset the session parameters")
                .page(Page.newBuilder()
                        .setDisplayName(UPDATE_CREDIT_LIMIT_START_PAGE)
                        .setEntryFulfillment(Fulfillment.newBuilder()
                                .addSetParameterActions(Fulfillment.SetParameterAction
                                        .newBuilder()
                                        .setParameter("card_last_4_digits")
                                        .setValue(Value.newBuilder()
                                                .setNullValue(NullValue.NULL_VALUE).build()
                                        )
                                        .build())
                                .addSetParameterActions(Fulfillment.SetParameterAction
                                        .newBuilder()
                                        .setParameter("new_credit_limit")
                                        .setValue(Value.newBuilder()
                                                .setNullValue(NullValue.NULL_VALUE).build()
                                        )
                                        .build())
                                .build())

                        .addTransitionRoutes(TransitionRoute.newBuilder()
                                .setCondition("true")
                                .setTargetPage(this.resourceDiscovery.discoverPage(FLOW_NAME, CAPTURE_CARD_DETAILS_PAGE))
                                .build())
                        .build())
                .startPage(true)
                .build();
    }


    private PageDefinition captureCardDetails() {
        return PageDefinition.builder()
                .description("Captures the last 4 digits of a credit card.")
                .page(Page.newBuilder()
                        .setDisplayName(CAPTURE_CARD_DETAILS_PAGE)
                        .setEntryFulfillment(Fulfillment.newBuilder()
                                .addMessages(ResponseMessage.newBuilder()
                                        .setText(ResponseMessage.Text
                                                .newBuilder()
                                                .addText("We are looking into your request !")
                                                .build())
                                        .build())
                                .build())
                        .setForm(Form.newBuilder()
                                .addParameters(Form.Parameter
                                        .newBuilder()
                                        .setDisplayName("card_last_4_digits")
                                        .setEntityType(this.resourceDiscovery
                                                .discoverEntityType("digits_4"))
                                        .setRequired(true)
                                        .setFillBehavior(Form.Parameter.FillBehavior
                                                .newBuilder()
                                                .setInitialPromptFulfillment(Fulfillment.newBuilder()
                                                        .addMessages(ResponseMessage.newBuilder()
                                                                .setText(ResponseMessage.Text
                                                                        .newBuilder()
                                                                        .addText("Enter the last 4 digits of your credit card.")
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .addRepromptEventHandlers(EventHandler.newBuilder()
                                                        .setEvent("sys.no-match-default")
                                                        .setTriggerFulfillment(Fulfillment.newBuilder()
                                                                .addMessages(ResponseMessage.newBuilder()
                                                                        .setText(ResponseMessage.Text
                                                                                .newBuilder()
                                                                                .addText("The input you have entered is invalid.")
                                                                                .build())
                                                                        .build())
                                                                .addMessages(ResponseMessage.newBuilder()
                                                                        .setText(ResponseMessage.Text.newBuilder()
                                                                                .addText("Please re-enter the last 4 digits of your credit card.")
                                                                                .build())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .addTransitionRoutes(TransitionRoute.newBuilder()
                                .setCondition("$page.params.status = \"FINAL\"")
                                .setTargetPage(this.resourceDiscovery.discoverPage(FLOW_NAME, CAPTURE_NEW_CREDIT_LIMIT))
                                .build())
                        .build())
                .build();
    }

    private PageDefinition captureNewCreditLimit() {
        return PageDefinition.builder()
                .description("Captures the new credit limit")
                .page(Page.newBuilder()
                        .setDisplayName(CAPTURE_NEW_CREDIT_LIMIT)
                        .setForm(Form.newBuilder()
                                .addParameters(Form.Parameter
                                        .newBuilder()
                                        .setDisplayName("new_credit_limit")
                                        .setEntityType(this.resourceDiscovery
                                                .discoverEntityType("digits_4"))
                                        .setRequired(true)
                                        .setFillBehavior(Form.Parameter.FillBehavior
                                                .newBuilder()
                                                .setInitialPromptFulfillment(Fulfillment.newBuilder()
                                                        .addMessages(ResponseMessage.newBuilder()
                                                                .setText(ResponseMessage.Text
                                                                        .newBuilder()
                                                                        .addText("Enter the new credit limit")
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .addRepromptEventHandlers(EventHandler.newBuilder()
                                                        .setEvent("sys.no-match-default")
                                                        .setTriggerFulfillment(Fulfillment.newBuilder()
                                                                .addMessages(ResponseMessage.newBuilder()
                                                                        .setText(ResponseMessage.Text
                                                                                .newBuilder()
                                                                                .addText("The input you have entered is invalid.")
                                                                                .build())
                                                                        .build())
                                                                .addMessages(ResponseMessage.newBuilder()
                                                                        .setText(ResponseMessage.Text.newBuilder()
                                                                                .addText("Please re-enter the new credit limit.")
                                                                                .build())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .addTransitionRoutes(TransitionRoute.newBuilder()
                                .setCondition("$page.params.status = \"FINAL\"")
                                .setTargetPage(this.resourceDiscovery.discoverPage(FLOW_NAME, CREATE_SERVICE_REQUEST_PAGE))
                                .build())
                        .build())
                .build();
    }

    private PageDefinition serviceRequestPage() {
        return PageDefinition.builder()
                .description("Create Service Request")
                .page(Page.newBuilder()
                        .setDisplayName(CREATE_SERVICE_REQUEST_PAGE)
                        .addTransitionRoutes(TransitionRoute.newBuilder()
                                .setCondition("true")
                                .setTriggerFulfillment(Fulfillment.newBuilder()
                                        .addMessages(ResponseMessage.newBuilder()
                                                .setText(ResponseMessage.Text
                                                        .newBuilder()
                                                        .addText("A service Request SR12345 has been raised to update the credit limit " +
                                                                "of your card ending with $session.params.card_last_4_digits " +
                                                                "to $session.params.new_credit_limit")
                                                        .build())
                                                .build())
                                        .addMessages(ResponseMessage.newBuilder()
                                                .setText(ResponseMessage.Text
                                                        .newBuilder()
                                                        .addText("Thanks for using online banking !")
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

    }
}
