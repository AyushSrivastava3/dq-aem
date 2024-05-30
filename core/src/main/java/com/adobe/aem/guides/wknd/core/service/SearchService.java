package com.adobe.aem.guides.wknd.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

@Component(service = SearchService.class)
public class SearchService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchService.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    private List<Map<String, String>> foundPages;
    public List<Map<String, String>> getFoundPages() {
        return foundPages;
    }

    public void performQuery(String pagePath) {
        foundPages = new ArrayList<>();
        ResourceResolver resourceResolver = null;

        try {
            resourceResolver = newResolver(resourceResolverFactory);

            Map<String, String> map = new HashMap<>();
            map.put("path", "/content/dq-aem/us/en");
            map.put("type", "cq:Page");
            map.put("group.p.or", "true");
            map.put("group.1_fulltext", pagePath);
            map.put("group.1_fulltext.relPath", "jcr:content");
            map.put("group.2_fulltext", pagePath);
            map.put("group.2_fulltext.relPath", "metadata");
            map.put("p.limit", "-1");

            Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));

            SearchResult result = query.getResult();
            LOG.info("Query executed: {}", result.getQueryStatement());

            Iterator<Resource> resourceIterator = result.getResources();
            while (resourceIterator.hasNext()) {
                Resource pageResource = resourceIterator.next();
                Node pageNode = pageResource.adaptTo(Node.class).getNode("jcr:content");

                String pageTitle = pageNode.getProperty("jcr:title").getString();
                LOG.info("Found page title: {}", pageTitle);

                Map<String, String> pageInfo = new HashMap<>();
                pageInfo.put("title", pageTitle);
                pageInfo.put("path", pageResource.getPath());
                foundPages.add(pageInfo);
                LOG.info("Added page info: {}", pageInfo);
            }
        } catch (Exception e) {
            LOG.error("Error occurred while performing query", e);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
                LOG.info("ResourceResolver closed");
            }
        }
    }

    public ResourceResolver newResolver(ResourceResolverFactory resourceResolverFactory) throws LoginException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");

        return resourceResolverFactory.getServiceResourceResolver(paramMap);
    }
}
