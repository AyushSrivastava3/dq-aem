package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
 
import javax.servlet.Servlet;
import javax.servlet.ServletException;
 
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.adobe.aem.guides.wknd.core.service.MyServiceAnnotation;
 
@Component(service = Servlet.class, 
/*
•	Purpose: Specifies the interfaces or classes that this component will register as OSGi services.
•	Example: service = { Servlet.class } indicates that this component will register as a servlet service.

*/

       property = { Constants.SERVICE_DESCRIPTION + "=Car Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/car" })
public class CarServletAnnotation extends SlingSafeMethodsServlet {
 
	private static final Logger LOG = LoggerFactory.getLogger(CarServletAnnotation.class);
 
	@Reference
	private MyServiceAnnotation myService;
	
 
	@Reference
	private ResourceResolverFactory resolverFactory;
	ResourceResolver resolver;
 
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Retrieve the propertyId parameter from the request
		String propertyId = request.getParameter("propertyId");
 
		if (propertyId != null && !propertyId.isEmpty()) {
			LOG.info("Received request with propertyId: {}", propertyId);
			// Add your logic here to process the request with the propertyId
		} else {
			LOG.error("No propertyId parameter found in the request");
			response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No propertyId parameter found in the request");
		}
 
		try {
			resolver = newResolver(resolverFactory);
			LOG.info("Message with propertyId: {}" + propertyId);
			myService.performQuery(propertyId);
			LOG.info("service calling done");
			/**
			 * Resource carResource = resolver
			 * .getResource("conf/mysite/settings/dam/cfm/models/cars2" +
			 * propertyId); if (carResource != null) { Car car =
			 * carResource.adaptTo(Car.class); if (car != null) { JSONObject jsonResponse =
			 * new JSONObject(); jsonResponse.put("brand", car.getBrand());
			 * jsonResponse.put("model", car.getModel()); jsonResponse.put("year",
			 * car.getYear()); response.getWriter().write(jsonResponse.toString()); return;
			 * } }
			 **/
			response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("service calling done");
		} catch (Exception e) {
			response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error occurred: " + e.getMessage());
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