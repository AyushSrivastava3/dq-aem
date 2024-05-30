package com.adobe.aem.guides.wknd.core.service;

import java.util.HashMap;
import java.util.Iterator;
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
 
@Component(service = MyServiceAnnotation.class)
public class MyServiceAnnotation {
 
	private static final Logger LOG = LoggerFactory.getLogger(MyServiceAnnotation.class);
 
	@Reference
	private ResourceResolverFactory resourceResolverFactory;
	/*
	@Reference: 
	Services can be consume using @Reference annotation in Servlet and other services 
	 */
		
 
	@Reference
	private QueryBuilder queryBuilder;
 
	public void performQuery(String propertyId) {
		LOG.info("inside the service method performQuery");
		ResourceResolver resourceResolver = null;
 
		try {
			// Create service user session
			// Map<String, Object> serviceUserParams = new HashMap<>();
			// serviceUserParams.put(ResourceResolverFactory.SUBSERVICE, "myserviceuser");
			resourceResolver = newResolver(resourceResolverFactory);// resourceResolverFactory.getServiceResourceResolver(serviceUserParams);
 
			LOG.info("111");
			// Create a query map
			Map<String, String> map = new HashMap<>();
			map.put("path", "/content/dam/dq-aem");
			map.put("type", "dam:Asset");
			map.put("property", "jcr:content/data/master/propertyId");
			map.put("property.value", propertyId);
			map.put("p.limit", "1"); // Limiting to 1 result
 
			// Create the query  
			Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
 
			LOG.info("222 ");
			// Execute the query
			SearchResult result = query.getResult();
			LOG.info("333 " + result.getQueryStatement());
			// Process the search results
			Iterator<Resource> resourceIterator = result.getResources();
			while (resourceIterator.hasNext()) {
				Resource resource = resourceIterator.next();
				// Process each resource
				LOG.info("Found resource: {}", resource.getPath());
				Node carNode = resource.adaptTo(Node.class);
				carNode = carNode.getNode("jcr:content/data/master");
				LOG.info("carNode path updated is : " + carNode.getPath());
				LOG.info("carMake is : " + carNode.getProperty("title").getValue().getString());
			}
			LOG.info("444" + result.getQueryStatement());
 
		} catch (Exception e) {
			LOG.error("Error occurred while performing query", e);
		} finally {
			// Always close the ResourceResolver
			if (resourceResolver != null && resourceResolver.isLive()) {
				resourceResolver.close();
			}
		}
	}
 
	public static final String GEEKS_SERVICE_USER = "geeksserviceuser";
 
	public ResourceResolver newResolver(ResourceResolverFactory resourceResolverFactory) throws LoginException {
		final Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, GEEKS_SERVICE_USER);
 
		// fetches the admin service resolver using service user.
		ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
		return resolver;
	}
 
}
 