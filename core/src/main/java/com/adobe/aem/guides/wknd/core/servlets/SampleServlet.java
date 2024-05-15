package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.service.AnotherService;
import com.adobe.aem.guides.wknd.core.service.SampleService;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/myServlet",
                "sling.servlet.methods=GET"
        }
)
public class SampleServlet extends SlingAllMethodsServlet {
    @Reference
    SampleService sampleService;
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
