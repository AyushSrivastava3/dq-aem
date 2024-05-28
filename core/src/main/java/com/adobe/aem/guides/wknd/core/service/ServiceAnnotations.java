package com.adobe.aem.guides.wknd.core.service;

import org.osgi.service.component.annotations.Component;
import com.adobe.aem.guides.wknd.core.service.osgi.OsgionfigurationAnnotations;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;


//@Component(service =ServiceAnnotations .class)

/*
 @Component :
The @Component annotation is part of the OSGi (Open Services Gateway initiative) Declarative Services specification. 
It is used to define a class as an OSGi component, which allows the class to be managed by the OSGi runtime. 
This annotation is crucial in AEM (Adobe Experience Manager) development as it simplifies the process of registering services,
 servlets, event listeners, and other components with the OSGi container.

 */


@Designate(ocd=OsgionfigurationAnnotations.class)
/*
 add configurations to OSGI Services with the help of @Designate annotation. 
 @Designate is must to have ocd(object class definition) property 
 */
public class ServiceAnnotations {
	
@Reference
SampleService sampleService;
/*
@Reference: 
Services can be consume using @Reference annotation in Servlet and other services 
 */
	    

public String getCompleteGreeting() 
{
String greeting = sampleService.getGreeting();
String formalGreeting = sampleService.getFormalGreeting();
return greeting + " " + formalGreeting;
	}
}
