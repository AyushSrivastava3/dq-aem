package com.adobe.aem.guides.wknd.core.service.osgi;



import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Sample Service Conf_today", description = "Configuration for Sample Service")
public @interface SampleServiceConfig {

    @AttributeDefinition(name = "Greeting Message", description = "Greeting message to be returned by the service")
    String greetingMessage() default "Hi Navatej !";

    @AttributeDefinition(name = "Formal Greeting Message", description = "Formal greeting message to be returned by the service")
    String formalGreetingMessage() default "welcome to digiquad solution.";
}

