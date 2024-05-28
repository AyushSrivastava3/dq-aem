package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.aem.guides.wknd.core.service.AnotherService;
import com.adobe.aem.guides.wknd.core.service.SampleService;

@Component(
        service = Servlet.class,
        /*
         •	Purpose: Specifies the interfaces or classes that this component will register as OSGi services.
         •	Example: service = { Servlet.class } indicates that this component will register as a servlet service.

         */
        property = {
                "sling.servlet.paths=/bin/myServlet",
                "sling.servlet.methods=GET"
        }
        )
public class ServletAnnotations  extends SlingAllMethodsServlet {
	
	
        	@Reference
            SampleService sampleService;
        	/*
        	 @Reference: 
             Services can be consume using @Reference annotation in Servlet and other services .
        	 */
            @Reference
            AnotherService anotherService;

            @Override
            protected void doGet(SlingHttpServletRequest request,
                                 SlingHttpServletResponse response) throws ServletException, IOException {

                String greeting = sampleService.getGreeting();
                String formalGreeting = sampleService.getFormalGreeting();
                response.getWriter().write(greeting + "\n" + formalGreeting+"\n");
                response.getWriter().write(anotherService.getCompleteGreeting());
            }

}
