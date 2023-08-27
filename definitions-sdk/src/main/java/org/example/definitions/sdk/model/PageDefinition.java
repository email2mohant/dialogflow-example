package org.example.definitions.sdk.model;

import lombok.Builder;
import com.google.cloud.dialogflow.cx.v3.Page;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class PageDefinition {
    @NonNull
    private Page page;
    @NonNull
    private String description;

    private boolean startPage = false;
}
