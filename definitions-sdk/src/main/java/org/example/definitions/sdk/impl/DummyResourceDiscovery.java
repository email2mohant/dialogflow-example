package org.example.definitions.sdk.impl;

import org.example.definitions.sdk.api.ResourceDiscovery;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class DummyResourceDiscovery implements ResourceDiscovery {
    @Override
    public String discoverFlow(String flowName) {
       throw new UnsupportedOperationException();
    }

    @Override
    public String discoverPage(String flowName, String pageName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String discoverEntityType(String entityType) {
        throw new UnsupportedOperationException();
    }
}
