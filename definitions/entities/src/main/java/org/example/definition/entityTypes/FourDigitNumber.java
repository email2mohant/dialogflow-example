package org.example.definition.entityTypes;

import com.google.cloud.dialogflow.cx.v3.EntityType;
import org.example.definitions.sdk.api.EntityTypeDefinitionProvider;
import org.example.definitions.sdk.model.EntityTypeDefinition;
import org.springframework.stereotype.Component;

@Component
public class FourDigitNumber implements EntityTypeDefinitionProvider {
    @Override
    public EntityTypeDefinition get() {
        return EntityTypeDefinition.builder()
                .description("Captures 4 digit number")
                .resourceName("digits_4")
                .entityType(EntityType.newBuilder()
                        .setKind(EntityType.Kind.KIND_REGEXP)
                        .addEntities(EntityType.Entity
                                .newBuilder()
                                .setValue("\\d{4}")
                                .addSynonyms("")
                                .build())
                        .build())
                .build();
    }
}
