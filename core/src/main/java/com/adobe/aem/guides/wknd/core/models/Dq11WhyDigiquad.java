
package com.adobe.aem.guides.wknd.core.models;



import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)

public class Dq11WhyDigiquad {

	@ValueMapValue
	private String title;
	
	@ValueMapValue
	private String fileReference;
	
	@ValueMapValue
	private String description;

	@ValueMapValue
	private String title1;
	
	@ValueMapValue
	private String fileReference1;

	@ValueMapValue
	private String description1;
	
	@ValueMapValue
	private String title2;

	@ValueMapValue
	private String fileReference2;
	
	@ValueMapValue
	private String description2;

	public String getTitle() {
		return title;
	}

	public String getFileReference() {
		return fileReference;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle1() {
		return title1;
	}

	public String getFileReference1() {
		return fileReference1;
	}

	public String getDescription1() {
		return description1;
	}

	public String getTitle2() {
		return title2;
	}

	public String getFileReference2() {
		return fileReference2;
	}

	public String getDescription2() {
		return description2;
	}
}
