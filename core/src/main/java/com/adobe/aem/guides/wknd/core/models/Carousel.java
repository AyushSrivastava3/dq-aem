package com.adobe.aem.guides.wknd.core.models;



import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class Carousel {

	
	@ValueMapValue
	private String title1;
	
	@ValueMapValue
	private String fileReference1;

	@ValueMapValue
	private String title2;
	
	@ValueMapValue
	private String fileReference2;

	public String getTitle1() {
		return title1;
	}

	public String getFileReference1() {
		return fileReference1;
	}

	public String getTitle2() {
		return title2;
	}

	public String getFileReference2() {
		return fileReference2;
	}




}