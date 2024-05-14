package com.adobe.aem.guides.wknd.core.models;



import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)

public class Learnmore {

	@ValueMapValue
	private String pagePath;
	
	@ValueMapValue
	private String pagePath1;
	
	@ValueMapValue
	private String pagePath2;
	
	@ValueMapValue
	private String pagePath3;
	
	@ValueMapValue
	private String fileReference;
	
	@ValueMapValue
	private String linkURL;

	public String getPagePath() {
		return pagePath;
	}

	public String getPagePath1() {
		return pagePath1;
	}

	public String getPagePath2() {
		return pagePath2;
	}

	public String getPagePath3() {
		return pagePath3;
	}

	public String getFileReference() {
		return fileReference;
	}

	public String getLinkURL() {
		return linkURL;
	}
}