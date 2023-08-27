package org.example.definitions.sdk.api;

import org.springframework.stereotype.Service;

/**
 * Resource Authors will be disco
 */
@Service
public interface ResourceDiscovery {
    public String discoverFlow(String flowName);

    public String discoverPage(String flowName, String pageName);

    public String discoverEntityType(String entityType);


}
