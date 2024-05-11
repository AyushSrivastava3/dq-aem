package com.adobe.aem.guides.wknd.core.models;



import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)

public class Cardone {

	@ValueMapValue
	private String fileReference;
	
	@ValueMapValue
	private String fileReference1;

	@ValueMapValue
	private String fileReferenc2;
	
	@ValueMapValue
	private String pagePath;

	@ValueMapValue
	private String text;
	
	@ValueMapValue
	private String pagePath1;

	@ValueMapValue
	private String text1;
	
	@ValueMapValue
	private String linkURL;

	public String getFileReference() {
		return fileReference;
	}

	public String getFileReference1() {
		return fileReference1;
	}

	public String getFileReferenc2() {
		return fileReferenc2;
	}

	public String getPagePath() {
		return pagePath;
	}

	public String getText() {
		return text;
	}

	public String getPagePath1() {
		return pagePath1;
	}

	public String getText1() {
		return text1;
	}

	public String getLinkURL() {
		return linkURL;
	}

}