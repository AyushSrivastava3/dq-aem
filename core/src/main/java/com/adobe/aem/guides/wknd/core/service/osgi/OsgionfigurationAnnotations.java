package com.adobe.aem.guides.wknd.core.service.osgi;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "Sample Service Conf_today", description = "Configuration for Sample Service")
/*
 @ObjectClassDefinition:
The @ObjectClassDefinition annotation is part of the OSGi Compendium and is used to define the metadata for configurations 
in OSGi Declarative Services (DS). This annotation is particularly useful in specifying the configuration properties that 
an OSGi component can receive.

 */

public @interface OsgionfigurationAnnotations {
	
	@AttributeDefinition(name = "Greeting Message", description = "Greeting message to be returned by the service")
    String greetingMessage() default "Hi Navatej !";
/*
@AttributeDefinition:
The @AttributeDefinition annotation is used in conjunction with the @ObjectClassDefinition annotation in OSGi to
define configuration properties for OSGi components.
 */

}
