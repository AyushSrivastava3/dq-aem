
package com.adobe.aem.guides.wknd.core.models;



import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
@Model(adaptables=Resource.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)

public class Dq6Card {

	@ValueMapValue
	private String cardtitle1;
	
	@ValueMapValue
	private String fileReferencecard1;
	
	@ValueMapValue
	private String carddescription1;
	
	@ValueMapValue
	private String cardtitle2;
	
	@ValueMapValue
	private String fileReferencecard2;
	
	@ValueMapValue
	private String carddescription2;
	
	@ValueMapValue
	private String cardtitle3;
	
	@ValueMapValue
	private String fileReferencecard3;
	
	@ValueMapValue
	private String carddescription3;

	public String getCardtitle1() {
		return cardtitle1;
	}

	public String getFileReferencecard1() {
		return fileReferencecard1;
	}

	public String getCarddescription1() {
		return carddescription1;
	}

	public String getCardtitle2() {
		return cardtitle2;
	}

	public String getFileReferencecard2() {
		return fileReferencecard2;
	}

	public String getCarddescription2() {
		return carddescription2;
	}

	public String getCardtitle3() {
		return cardtitle3;
	}

	public String getFileReferencecard3() {
		return fileReferencecard3;
	}

	public String getCarddescription3() {
		return carddescription3;
	}
	
}
