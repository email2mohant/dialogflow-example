package org.example.services.config;

import com.google.cloud.dialogflow.cx.v3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ServiceConfiguration {

    @Bean
    public FlowsClient flowsClient(String endPoint) throws IOException {
        return FlowsClient.create(FlowsSettings.newBuilder()
                .setEndpoint(endPoint)
                .build());
    }


    @Bean
    public AgentsClient agentsClient(String endPoint) throws IOException {
        return AgentsClient.create(AgentsSettings.newBuilder().setEndpoint(endPoint).build());
    }

    @Bean
    public PagesClient pagesClient(String endPoint) throws IOException {
        return PagesClient.create(PagesSettings.newBuilder()
                .setEndpoint(endPoint)
                .build());
    }

    @Bean
    public EntityTypesClient entityTypesClient(String endPoint) throws IOException {
        return EntityTypesClient.create(EntityTypesSettings.newBuilder()
                .setEndpoint(endPoint)
                .build()
        );
    }

    @Bean
    public IntentsClient intentsClient(String endPoint) throws IOException {
        return IntentsClient.create(IntentsSettings.newBuilder()
                .setEndpoint(endPoint)
                .build()
        );
    }

    @Bean
    public String getEndPoint(@Value("${dialogflow.location}") String location) {
        String endPointPrefix = location.toLowerCase().equals("global") ? "" : location + "-";
        return endPointPrefix + "dialogflow.googleapis.com:443";

    }
}
