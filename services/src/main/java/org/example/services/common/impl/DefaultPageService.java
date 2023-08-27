package org.example.services.common.impl;

import com.google.cloud.dialogflow.cx.v3.*;
import org.example.definitions.sdk.model.PageDefinition;
import org.example.services.common.api.PageService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Service
public class DefaultPageService implements PageService {
    private final PagesClient pagesClient;

    public DefaultPageService(PagesClient pagesClient){
        this.pagesClient = pagesClient;
    }
    @Override
    public Page createPage(Flow parent, PageDefinition pageDefinition) {
        return this.pagesClient.createPage(CreatePageRequest.newBuilder()
                .setParent(parent.getName())
                .setPage(pageDefinition.getPage())
                .build());
    }

    @Override
    public Optional<Page> getPage(Flow flow, String pageName) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.pagesClient.listPages(flow.getName()).iterateAll().iterator(), 0), false)
                .filter(page -> page.getDisplayName().equalsIgnoreCase(pageName))
                .findFirst();
    }


}
