package com.adobe.aem.guides.wknd.core.service;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.models.OsgiServiceInterface;
import com.adobe.aem.guides.wknd.core.service.osgi.OsgiConfigurations;

@Component(service = OsgiServiceInterface.class,immediate=true)
/*
@Component :
The @Component annotation is part of the OSGi (Open Services Gateway initiative) Declarative Services specification. 
It is used to define a class as an OSGi component, which allows the class to be managed by the OSGi runtime. 
This annotation is crucial in AEM (Adobe Experience Manager) development as it simplifies the process of registering services,
servlets, event listeners, and other components with the OSGi container.

*/


@Designate(ocd = OsgiConfigurations.class)

/*
add configurations to OSGI Services with the help of @Designate annotation. 
@Designate is must to have ocd(object class definition) property 
*/
public class OsgiServiceImplementation implements OsgiServiceInterface{
	
	private static final Logger LOG = LoggerFactory.getLogger(OsgiServiceInterface.class);
	
	private OsgiConfigurations configurations;
	
	@Activate
/*
@Activate
This annotation is used to notify your component that it is now loaded, resolved and ready to provide service. 
You use this method to do some final setup in your component.
*/
	private void activate(OsgiConfigurations config) {
			configurations = config;
	}
	
	


	@Override
	public String getCourseDetails() {
		LOG.info("\n=============Implement Two=========================");

		return configurations.courseDetails();
	}


	@Override
	public String getCourseName() {
		// TODO Auto-generated method stub
		return configurations.courseName();
	}



	@Override
	public String getCourseContent() {
		// TODO Auto-generated method stub
		return configurations.courseContent();
	}
}