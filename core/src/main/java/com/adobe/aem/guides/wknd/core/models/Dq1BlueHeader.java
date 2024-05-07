package com.adobe.aem.guides.wknd.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class Dq1BlueHeader {

	@ValueMapValue
	private String title;
	
	@ValueMapValue
	private String email;

	public String getTitle() {
		return title;
	}

	public String getEmail() {
		return email;
	}	
	
	
}
