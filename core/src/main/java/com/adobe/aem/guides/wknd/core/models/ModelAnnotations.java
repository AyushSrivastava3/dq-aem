package com.adobe.aem.guides.wknd.core.models;

import javax.inject.Inject;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.service.osgi.SampleConfig;


@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
/*
The @Model annotation in Sling Models is essential for defining a Java class as a Sling Model. This annotation is part of the Apache
Sling Models API and is used to specify that a class can be adapted from certain types of objects (like Resource, Request, etc.) and 
to handle dependency injection of properties and services.This annotation has several attributes, and adaptables is one of them.
 It tells Sling what type of object this model can adapt from .
 */

public class ModelAnnotations {
	
	 @OSGiService
	 SampleConfig sampleConfig;
	 /*
	   @OSGiService :
       Use @OSGiService annotation  to inject service in sling model

	  */
	 
	 
	@Inject
	private String text;
/*
@Inject :
This annotation is used to inject OSGi services and other dependencies into the Sling Model.
 */
	
	@ValueMapValue
	private String description;
/*
@ValueMapValue:
This simply used to map our resource to their respective java variable, as we add String myname in below ValueMapValue annotation.
In this step sling search myname key from the resource  and map their value to respective java variable.
*/
	

	public String getText() {
		return text;
	}

	public String getDescription() {
		return description;
	}
	
}
