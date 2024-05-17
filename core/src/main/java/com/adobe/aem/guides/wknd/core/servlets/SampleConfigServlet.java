//package com.adobe.aem.guides.wknd.core.servlets;
//
//import com.adobe.aem.guides.wknd.core.service.osgi.SampleServiceConfig;
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.osgi.resource.Resource;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//import java.io.IOException;
//@Component(
//        service = Servlet.class,
//        property = {
//                "sling.servlet.paths=/bin/myServlet",
//                "sling.servlet.methods=GET"
//        }
//)
//
//public class SampleConfigServlet {
//    @Reference
//    SampleServiceConfig sampleServiceConfig;
//
//
//   @Override
//    protected void doGet(final SlingHttpServletRequest request,
//                         final SlingHttpServletResponse response) throws ServletException, IOException {
//        final Resource resource= (Resource) request.getResource();
//        String greeting = sampleServiceConfig.greetingMessage();
//        String formalGreeting = sampleServiceConfig.formalGreetingMessage();
//        response.getWriter().write(greeting + "\n" + formalGreeting+"\n");
//        //response.getWriter().write(anotherService.getCompleteGreeting());
//    }
//
//}



package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.service.AnotherService;
import com.adobe.aem.guides.wknd.core.service.SampleService;
import com.adobe.aem.guides.wknd.core.service.osgi.SampleServiceConfig;
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
                "sling.servlet.paths=bin/config/myservlet",
                "sling.servlet.methods=GET"
        }
)
public class SampleConfigServlet extends SlingAllMethodsServlet {
    @Reference
    SampleServiceConfig sampleServiceConfig;

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws ServletException, IOException {

        String greeting = sampleServiceConfig.greetingMessage();
        String formalGreeting = sampleServiceConfig.formalGreetingMessage();
        response.getWriter().write(greeting + "\n" + formalGreeting+"\n");

    }

}
