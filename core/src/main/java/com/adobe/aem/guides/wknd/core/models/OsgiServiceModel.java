package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.models.OsgiServiceInterface;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class OsgiServiceModel {
	
	private static final Logger LOG = LoggerFactory.getLogger(OsgiServiceModel.class);

	
	@OSGiService
	private OsgiServiceInterface osgiServiceInterface;
	 /*
	   @OSGiService :
    Use @OSGiService annotation  to inject service in sling model

	  */
	
	private String courseName;
	
	private String courseContent;
	
	private String courseDetails;

	public String getCourseName() {
		LOG.info("\n=============Implement One=========================");
		return osgiServiceInterface.getCourseName();
	}

	public String getCourseContent() {
		LOG.info("\n=============Implement two=========================");
		return osgiServiceInterface.getCourseContent();
	}

	public String getCourseDetails() {
		LOG.info("\n=============Implement three=========================");
		return osgiServiceInterface.getCourseDetails();
	}



}