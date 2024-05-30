package com.adobe.aem.guides.wknd.core.service.osgi;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "AEM Train", description = "This is an example learning")
/*
@ObjectClassDefinition:
The @ObjectClassDefinition annotation is part of the OSGi Compendium and is used to define the metadata for configurations 
in OSGi Declarative Services (DS). This annotation is particularly useful in specifying the configuration properties that 
an OSGi component can receive.

*/

public @interface OsgiConfigurations {
	

    @AttributeDefinition(name = "Course Name") String courseName() default "";

    @AttributeDefinition(name = "Course Content") String courseContent() default "AEM Frontend and Backend concepts";

    @AttributeDefinition(name = "Course Duration") String courseDetails() default "Component, Templates, ClientLibs, Sling Models, OSGI Services";

    /*
    @AttributeDefinition:
    The @AttributeDefinition annotation is used in conjunction with the @ObjectClassDefinition annotation in OSGi to
    define configuration properties for OSGi components.
     */
}