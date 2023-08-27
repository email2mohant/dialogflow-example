package org.example.services.common.api;

import com.google.cloud.dialogflow.cx.v3.Flow;
import com.google.cloud.dialogflow.cx.v3.Page;
import org.example.definitions.sdk.model.PageDefinition;

import java.util.Optional;

public interface PageService {
    Page createPage(Flow parent, PageDefinition pageDefinition);

    Optional<Page> getPage(Flow flow, String pageName);

}
