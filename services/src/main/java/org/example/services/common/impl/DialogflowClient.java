package org.example.services.common.impl;

import com.google.cloud.dialogflow.cx.v3.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class DialogflowClient {
    @Autowired
    private AgentsClient agentsClient;
    @Autowired
    private FlowsClient flowsClient;
    @Autowired
    private PagesClient pagesClient;
    @Autowired
    private EntityTypesClient entityTypesClient;
    @Autowired
    private IntentsClient intentsClient;


}
