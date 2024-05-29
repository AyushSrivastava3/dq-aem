package com.adobe.aem.guides.wknd.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.service.SearchService;

@Model(adaptables = SlingHttpServletRequest.class)
public class SearchModel {

    private static final Logger LOG = LoggerFactory.getLogger(SearchModel.class);

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private SearchService pageService;

    @OSGiService
    private ResourceResolverFactory resolverFactory;

    private String pagePath;
    private List<Map<String, String>> foundPages;

    public List<Map<String, String>> getFoundPages() {
        return foundPages;
    }

    public String getPagePath() {
        return pagePath;
    }

    @PostConstruct
    protected void init() {
        // Retrieve the pagePath parameter from the request
        pagePath = request.getParameter("pagePath");

        if (pagePath != null && !pagePath.isEmpty()) {
            LOG.info("Received request with pagePath: {}", pagePath);
            try {
                ResourceResolver resolver = newResolver(resolverFactory);
                pageService.performQuery(pagePath);
                foundPages = pageService.getFoundPages();
                LOG.info("Service calling done");
            } catch (Exception e) {
                LOG.error("Error occurred while processing the request", e);
            }
        } else {
            LOG.error("No pagePath parameter found in the request");
        }
    }

    public static final String GEEKS_SERVICE_USER = "geeksserviceuser";

    public ResourceResolver newResolver(ResourceResolverFactory resourceResolverFactory) throws LoginException {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, GEEKS_SERVICE_USER);

        return resourceResolverFactory.getServiceResourceResolver(paramMap);
    }
}
